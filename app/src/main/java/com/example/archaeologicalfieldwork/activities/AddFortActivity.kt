package com.example.archaeologicalfieldwork.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.adapter.NotesAdapter
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.google.android.gms.maps.*
import kotlinx.android.synthetic.main.activity_addfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.text.SimpleDateFormat

class AddFortActivity : AppCompatActivity(),AnkoLogger, OnMapReadyCallback {

    var hillfort = HillFortModel()
    lateinit var presenter: FortPresenter
    lateinit var context: Context
    var date = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addfort)

        presenter = FortPresenter(this)

        context = this

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        info("Add HillFort Started")

//      When editing a hillfort this is checked or the the location is changed
        if(intent.hasExtra("hillfort_edit") || intent.hasExtra("location")){
            hillfort = intent.extras?.getParcelable<HillFortModel>("hillfort_edit")!!
            presenter.doEditHillfort(hillfort)
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
            presenter.doMapButton()
        }
//      Notes Adapter
        val layoutManager = LinearLayoutManager(this)

        mNotesRecyclerView.layoutManager = layoutManager as RecyclerView.LayoutManager?
        mNotesRecyclerView.adapter = NotesAdapter(hillfort.note)

//      Deletes hillfort
        mHillFortBtnDelete.setOnClickListener {
            presenter.doRemoveHillfort(hillfort)
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
            presenter.doAddFort(date,hillfort)
        }

//      Removes image from viewpager
        mHillFortRemoveImage.setOnClickListener {
            presenter.doRemoveImage(mAddFortImagePager.currentItem)
        }
//      Adds image to view pager
        mAddImage.setOnClickListener{
            presenter.doSelectImage()
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
    presenter.doMapReady(googleMap)
}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data != null){
            presenter.doActivityResult(requestCode,resultCode,data,this)
        }
    }

}
