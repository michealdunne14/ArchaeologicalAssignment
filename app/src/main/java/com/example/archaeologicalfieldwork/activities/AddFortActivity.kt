package com.example.archaeologicalfieldwork.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.adapter.ImageAdapter
import com.example.archaeologicalfieldwork.adapter.NotesAdapter
import com.example.archaeologicalfieldwork.helper.showImagePicker
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Location
import com.example.archaeologicalfieldwork.models.UserModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_addfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import java.text.ParseException
import java.text.SimpleDateFormat

class AddFortActivity : AppCompatActivity(),AnkoLogger, OnMapReadyCallback {

    var hillfort = HillFortModel()
    var user = UserModel()
    var location = Location(52.245696, -7.139102, 15f)
    lateinit var app : MainApp
    private lateinit var mMapGoogle: GoogleMap
    var date = String()

    var editinghillfort = false

    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addfort)
        app = application as MainApp

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        info("Add HillFort Started")
        user = app.user

//      When editing a hillfort this is checked or the the location is changed
        if(intent.hasExtra("hillfort_edit") || intent.hasExtra("location")){
            hillfort = intent.extras?.getParcelable<HillFortModel>("hillfort_edit")!!
            mHillFortName.setText(hillfort.name)
            mHillFortDescription.setText(hillfort.description)
            mHillFortVisitedCheckbox.isChecked = hillfort.visitCheck
            mHillFortLocationText.text = hillfort.location.toString()
            location = hillfort.location
//          Formatting date to long to pass in to calender
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            try {
                val date = formatter.parse(hillfort.datevisted)
                val dateInLong = date.time
                mHillFortDatePicker.date = dateInLong
            } catch (e: ParseException) {
                e.printStackTrace()
            }


//          View Pager for multiple images
            val viewPager = findViewById<ViewPager>(R.id.mAddFortImagePager)
            val adapter =
                ImageAdapter(this, hillfort.imageStore)
            viewPager.adapter = adapter
            editinghillfort = true
            mHillFortBtnAdd.text = getString(R.string.save_hillfort)
            mHillFortBtnDelete.visibility = View.VISIBLE
        }else{
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val selectedDate = sdf.format(mHillFortDatePicker.date)
            date = selectedDate
        }

//      Allows map fragment to be on add Fort Activty
        val mMap = (supportFragmentManager.findFragmentById(R.id.mMapFragment) as SupportMapFragment)
        mMap.getMapAsync(this)
//      Hides hillfort date picker. reason done here and not in xml is it does not adjust items below it leaving a huge blank.
        mHillFortDatePicker.visibility = View.GONE


//      Starts Map Activity
        mMapButton.setOnClickListener {
            info { "Map Activity Started" }
            if (hillfort.location.lat != 0.0 && hillfort.location.lng != 0.0) {
                startActivityForResult(intentFor<MapsActivity>().putExtra("location", hillfort.location), LOCATION_REQUEST)
            }else{
                startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
            }
        }
//      Notes Adapter
        val layoutManager = LinearLayoutManager(this)

        mNotesRecyclerView.layoutManager = layoutManager as RecyclerView.LayoutManager?
        mNotesRecyclerView.adapter = NotesAdapter(hillfort.note)

//      Deletes hillfort
        mHillFortBtnDelete.setOnClickListener {
            app.hillforts.deleteHillforts(hillfort.copy(),user)
            startActivity(Intent(baseContext,MainActivity::class.java))
        }

//      Checkbox for selecting date
        mHillFortAddDate.setOnClickListener {
            if (mHillFortAddDate.isChecked){
                mHillFortDatePicker.visibility = View.VISIBLE
            }else{
                mHillFortDatePicker.visibility = View.GONE
            }
        }

//      sets the date of the calender when changed.
        mHillFortDatePicker.setOnDateChangeListener(CalendarView.OnDateChangeListener(){
            view, year, month, dayOfMonth ->
            date = "$dayOfMonth/$month/$year"
        })

//      Adds Hillforts to JSON
        mHillFortBtnAdd.setOnClickListener{
            hillfort.name = mHillFortName.text.toString()
            hillfort.description = mHillFortDescription.text.toString()
            if (location.lat != 52.245696 && location.lng != -7.139102) {
                hillfort.location = location
            }
            hillfort.visitCheck = mHillFortVisitedCheckbox.isChecked
            if(mHillFortAddDate.isChecked) {
                hillfort.datevisted = date
            }

            if (hillfort.name.isNotEmpty() && hillfort.imageStore.isNotEmpty()){
                if(editinghillfort){
                    app.hillforts.updateHillforts(hillfort.copy(),user)
                }else{
                    app.hillforts.createHillfort(hillfort.copy(),user)
                }
                info { "add Button Pressed: ${hillfort}" }
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                toast(getString(R.string.addfort_entertitleandimage))
            }
        }

//      Removes image from viewpager
        mHillFortRemoveImage.setOnClickListener {
            if(hillfort.imageStore.size == 0){
                toast("No Images to Delete")
            }else {
                hillfort.imageStore.removeAt(mAddFortImagePager.currentItem)
                val viewPager = findViewById<ViewPager>(R.id.mAddFortImagePager)
                val adapter = ImageAdapter(this, hillfort.imageStore)
                viewPager.adapter = adapter
            }
        }
//      Adds image to view pager
        mAddImage.setOnClickListener{
            showImagePicker(this, IMAGE_REQUEST)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.addfort_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

//  Map
    override fun onMapReady(googleMap: GoogleMap) {
        mMapGoogle = googleMap
        mMapGoogle.clear()
        if (hillfort.location.lat != 0.0 && hillfort.location.lng != 0.0) {
            val loc = LatLng(hillfort.location.lat, hillfort.location.lng)
            val options = MarkerOptions()
                .title("HillForts")
                .snippet("GPS : " + loc.toString())
                .position(loc)
            mMapGoogle.addMarker(options)
            mMapGoogle.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, hillfort.location.zoom))
        }else {
            val loc = LatLng(location.lat,location.lng)
            val options = MarkerOptions()
                .title("HillForts")
                .snippet("GPS : " + loc.toString())
                .position(loc)
            mMapGoogle.addMarker(options)
            mMapGoogle.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
        }
    }

//  When a result comes back
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            IMAGE_REQUEST -> {
                if (data != null){
                    hillfort.image = data.data.toString()
                    hillfort.imageStore.add(hillfort.image)
                    val viewPager = findViewById<ViewPager>(R.id.mAddFortImagePager)
                    val adapter = ImageAdapter(
                        this,
                        hillfort.imageStore
                    )
                    viewPager.adapter = adapter
                }
            }            LOCATION_REQUEST -> {
            if (data != null) {
                location = data.extras?.getParcelable<Location>("location")!!
                mHillFortLocationText.text = location.toString()
            }
        }
        }
        val mMap = (supportFragmentManager.findFragmentById(R.id.mMapFragment) as SupportMapFragment)
        mMap.getMapAsync(this)
    }
}
