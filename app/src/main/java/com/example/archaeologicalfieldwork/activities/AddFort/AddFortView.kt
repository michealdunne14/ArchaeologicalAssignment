package com.example.archaeologicalfieldwork.activities.AddFort

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CalendarView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseActivity.BaseView
import com.example.archaeologicalfieldwork.activities.Main.MainView
import com.example.archaeologicalfieldwork.adapter.ImageAdapter
import com.example.archaeologicalfieldwork.adapter.NotesAdapter
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Location
import com.example.archaeologicalfieldwork.models.Notes
import com.google.android.gms.maps.*
import kotlinx.android.synthetic.main.activity_addfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class AddFortView : BaseView(),AnkoLogger {

    var hillfort = HillFortModel()
    lateinit var presenter: FortPresenter
    var date = String()
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addfort)
        mapView.onCreate(savedInstanceState)
        //      Sets up the presenter
        presenter = initPresenter(FortPresenter(this)) as FortPresenter
//      Sets up map
        mapView.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
            presenter.updateLocation()
            it.setOnMapClickListener { presenter.doSetLocation() }
        }
//      Sets Toolbar Title
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        info("Add HillFort Started")

//      Hides hillfort date picker. reason done here and not in xml is it does not adjust items below it leaving a huge blank.
        mHillFortDatePicker.visibility = View.GONE


//      Deletes hillfort
        mHillFortBtnDelete.setOnClickListener {
            presenter.doDelete()
            startActivity(Intent(baseContext,
                MainView::class.java))
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
            hillfort.description = mHillFortDescription.text.toString()
            hillfort.name = mHillFortName.text.toString()
            hillfort.rating = mRatingBar.rating.toInt()
            hillfort.visitCheck = mHillFortVisitedCheckbox.isChecked
            hillfort.starCheck = mHillFortStarCheckbox.isChecked
            if(mHillFortAddDate.isChecked) {
                hillfort.datevisted = date
            }
            presenter.doAddFort(hillfort)
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

//  Put the details of the hillfort in to the view
    override fun putHillfort(hillFortModel: HillFortModel) {
        mHillFortName.setText(hillFortModel.name)
        mHillFortDescription.setText(hillFortModel.description)
        mHillFortVisitedCheckbox.isChecked = hillFortModel.visitCheck
        mHillFortStarCheckbox.isChecked = hillFortModel.starCheck
        mRatingBar.rating = hillFortModel.rating.toFloat()
    }
//  add images to the Hillfort using the adapter
    override fun addImages(listofImages: ArrayList<String>){
        val viewPager = findViewById<ViewPager>(R.id.mAddFortImagePager)
        val adapter = ImageAdapter(this, listofImages)
        viewPager.adapter = adapter
    }
//  Show the location in text
    override fun showLocation(location: Location){
        mHillFortLocationText.text = location.toString()
    }
//
    override fun showHillfortUpdate(){
        mHillFortBtnAdd.text = getString(R.string.save_hillfort)
        mHillFortBtnDelete.visibility = View.VISIBLE
    }
//  Show result in the console
    override fun showResult(hillFortModel: HillFortModel){
        info { "add Button Pressed: ${hillfort}" }
        setResult(Activity.RESULT_OK)
        finish()
    }
//  Cancel the Selected Hillfort
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data != null){
            presenter.doActivityResult(requestCode,resultCode,data,this,hillfort,this)
        }
    }

    //  Show Notes for this hillfort
    override fun showNotes(notes: List<Notes>) {
        val layoutManager = LinearLayoutManager(this)
        mNotesRecyclerView.layoutManager = layoutManager as RecyclerView.LayoutManager?
        mNotesRecyclerView.adapter = NotesAdapter(notes)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
