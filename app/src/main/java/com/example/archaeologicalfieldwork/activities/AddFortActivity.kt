package com.example.archaeologicalfieldwork.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.helper.showImagePicker
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Location
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_addfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import java.util.*

class AddFortActivity : AppCompatActivity(),AnkoLogger, OnMapReadyCallback {

    var hillfort = HillFortModel()
    var location = Location(52.245696, -7.139102, 15f)
    lateinit var app : MainApp
    private lateinit var mMapGoogle: GoogleMap

    var editinghillfort = false
    private val imageList = ArrayList<String>()

    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addfort)
        app = application as MainApp

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        info("Add Hill Fort Started..")

        if(intent.hasExtra("hillfort_edit")){
            hillfort = intent.extras?.getParcelable<HillFortModel>("hillfort_edit")!!
            mHillFortName.setText(hillfort.name)
            mHillFortDescription.setText(hillfort.description)
            mVisitedCheckbox.isChecked = hillfort.visitCheck
            mHillFortLocationText.text = hillfort.location.toString()

            val viewPager = findViewById<ViewPager>(R.id.mAddFortImagePager)
            val adapter = ImageAdapter(this,hillfort.imageStore)
            viewPager.adapter = adapter
            editinghillfort = true
            mBtnAdd.text = getString(R.string.save_hillfort)

        }

        mMapButton.setOnClickListener {
            startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
        }

        val layoutManager = LinearLayoutManager(this)

        mNotesRecyclerView.layoutManager = layoutManager as RecyclerView.LayoutManager?
        mNotesRecyclerView.adapter = NotesAdapter(hillfort.note)

        val mMap = (supportFragmentManager
            .findFragmentById(R.id.mMapFragment) as SupportMapFragment)
        mMap.getMapAsync(this)


        mBtnAdd.setOnClickListener(){
            hillfort.name = mHillFortName.text.toString()
            hillfort.description = mHillFortDescription.text.toString()
            hillfort.location = location
            hillfort.visitCheck = mVisitedCheckbox.isChecked


            for (images in imageList) {
                hillfort.imageStore.add(images)
            }

            if (hillfort.name.isNotEmpty() && hillfort.imageStore.isNotEmpty()){
                if(editinghillfort){
                    app.hillforts.update(hillfort.copy())
                }else{
                    app.hillforts.create(hillfort.copy())
                }
                info { "add Button Pressed: ${hillfort}" }
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                toast(getString(R.string.addfort_entertitleandimage))
            }
        }

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

    override fun onMapReady(googleMap: GoogleMap) {
        mMapGoogle = googleMap

        if(hillfort.location.lat != 0.0 && hillfort.location.lng != 0.0){
            val loc = LatLng(hillfort.location.lat, hillfort.location.lng)
            val options = MarkerOptions()
                .title("HillForts")
                .snippet("GPS : " + loc.toString())
                .position(loc)
            mMapGoogle.addMarker(options)
            mMapGoogle.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, hillfort.location.zoom))
        }else if (location.lat != 0.0 && location.lng != 0.0) {
            val loc = LatLng(location.lat, location.lng)
            val options = MarkerOptions()
                .title("HillForts")
                .snippet("GPS : " + loc.toString())
                .position(loc)
            mMapGoogle.addMarker(options)
            mMapGoogle.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            IMAGE_REQUEST -> {
                if (data != null){
                    hillfort.image = data.data.toString()
//                    mHillFortImage.setImageBitmap(readImage(this,resultCode,data))
                    imageList.add(hillfort.image)
                    val viewPager = findViewById<ViewPager>(R.id.mAddFortImagePager)
                    val adapter = ImageAdapter(this,imageList)
                    viewPager.adapter = adapter
                }
            }            LOCATION_REQUEST -> {
            if (data != null) {
                location = data.extras?.getParcelable<Location>("location")!!
                mHillFortLocationText.text = location.toString()
            }
        }
        }
        val mMap = (supportFragmentManager
            .findFragmentById(R.id.mMapFragment) as SupportMapFragment)
        mMap.getMapAsync(this)
    }
}
