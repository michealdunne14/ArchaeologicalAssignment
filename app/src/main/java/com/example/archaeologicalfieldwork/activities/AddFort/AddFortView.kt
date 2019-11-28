package com.example.archaeologicalfieldwork.activities.AddFort

import android.app.Activity
import android.content.Context
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
import com.example.archaeologicalfieldwork.activities.BaseView
import com.example.archaeologicalfieldwork.activities.Main.MainView
import com.example.archaeologicalfieldwork.adapter.ImageAdapter
import com.example.archaeologicalfieldwork.adapter.NotesAdapter
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Location
import com.google.android.gms.maps.*
import kotlinx.android.synthetic.main.activity_addfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.text.SimpleDateFormat

class AddFortView : BaseView(),AnkoLogger {

    var hillfort = HillFortModel()
    lateinit var presenter: FortPresenter
    lateinit var context: Context
    var date = String()
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addfort)
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
            it.setOnMapClickListener { presenter.doSetLocation() }

        }

        presenter = initPresenter(FortPresenter(this)) as FortPresenter

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

//      Hides hillfort date picker. reason done here and not in xml is it does not adjust items below it leaving a huge blank.
        mHillFortDatePicker.visibility = View.GONE


//      Notes Adapter
        val layoutManager = LinearLayoutManager(this)

        mNotesRecyclerView.layoutManager = layoutManager as RecyclerView.LayoutManager?
        mNotesRecyclerView.adapter = NotesAdapter(hillfort.note)

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
            presenter.doAddFort(date,hillfort)
        }

//      Removes image from viewpager
        mHillFortRemoveImage.setOnClickListener {
            presenter.doRemoveImage(mAddFortImagePager.currentItem,hillfort)
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

    override fun showHillfort(hillFortModel: HillFortModel) {
        mHillFortName.setText(hillFortModel.name)
        mHillFortDescription.setText(hillFortModel.description)
        mHillFortVisitedCheckbox.isChecked = hillFortModel.visitCheck
        mHillFortLocationText.text = hillFortModel.location.toString()
    }

    override fun showImages(){
        val viewPager = findViewById<ViewPager>(R.id.mAddFortImagePager)
        val adapter = ImageAdapter(context, hillfort.imageStore)
        viewPager.adapter = adapter
    }

    override fun showLocation(hillFortModel: HillFortModel, location: Location){
        mHillFortLocationText.text = location.toString()
    }

    override fun showHillfortAdd(){
        mHillFortBtnAdd.text = getString(R.string.save_hillfort)
        mHillFortBtnDelete.visibility = View.VISIBLE
    }

    override fun showResult(hillFortModel: HillFortModel){
        info { "add Button Pressed: ${hillfort}" }
        setResult(Activity.RESULT_OK)
        finish()
    }

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
            presenter.doActivityResult(requestCode,resultCode,data,this,hillfort,context)
        }
    }

    override fun onBackPressed() {
        presenter.doCancel()
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
