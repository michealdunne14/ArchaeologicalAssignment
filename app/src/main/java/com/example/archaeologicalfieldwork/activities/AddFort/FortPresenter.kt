package com.example.archaeologicalfieldwork.activities.AddFort

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BasePresenter
import com.example.archaeologicalfieldwork.activities.BaseView
import com.example.archaeologicalfieldwork.activities.EditLocation.EditLocationView
import com.example.archaeologicalfieldwork.adapter.ImageAdapter
import com.example.archaeologicalfieldwork.helper.showImagePicker
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Location
import com.example.archaeologicalfieldwork.models.UserModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_addfort.*
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import java.text.ParseException
import java.text.SimpleDateFormat

class FortPresenter(view: BaseView):BasePresenter(view){
    var user = UserModel()
    var hillfort = HillFortModel()
    var location = Location(52.245696, -7.139102, 15f)
    override var app : MainApp = view.application as MainApp
    private lateinit var mMapGoogle: GoogleMap

    var editinghillfort = false

    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2

    init {
        user = app.user
    }

    fun doSelectImage(){
        showImagePicker(view,IMAGE_REQUEST)
    }

    fun doRemoveHillfort(hillfort: HillFortModel) {
        app.hillforts.deleteHillforts(hillfort.copy(),user)
    }

    fun doMapButton(){
        view.info { "Map Activity Started" }
        if (hillfort.location.lat != 0.0 && hillfort.location.lng != 0.0) {
            view.startActivityForResult(view.intentFor<EditLocationView>().putExtra("location", hillfort.location), LOCATION_REQUEST)
        }else{
            view.startActivityForResult(view.intentFor<EditLocationView>().putExtra("location", location), LOCATION_REQUEST)
        }
    }

    fun doRemoveImage(currentItem: Int,context: Context) {
        if(hillfort.imageStore.size == 0){
            view.toast(view.getString(R.string.deleteimages))
        }else {
            hillfort.imageStore.removeAt(currentItem)
            val viewPager = view.findViewById<ViewPager>(R.id.mAddFortImagePager)
            val adapter = ImageAdapter(context, hillfort.imageStore)
            viewPager.adapter = adapter
        }
    }

    fun doEditHillfort(hillfort: HillFortModel,context: Context) {
        view.showHillfort(hillfort)
        location = hillfort.location
//          Formatting date to long to pass in to calender
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        try {
            val date = formatter.parse(this.hillfort.datevisted)
            val dateInLong = date.time
            view.mHillFortDatePicker.date = dateInLong
        } catch (e: ParseException) {
            e.printStackTrace()
        }


//          View Pager for multiple images
        val viewPager = view.findViewById<ViewPager>(R.id.mAddFortImagePager)
        val adapter = ImageAdapter(context, hillfort.imageStore)
        viewPager.adapter = adapter
        editinghillfort = true
        view.mHillFortBtnAdd.text = view.getString(R.string.save_hillfort)
        view.mHillFortBtnDelete.visibility = View.VISIBLE
    }

    fun doMapReady(googleMap: GoogleMap){
        mMapGoogle = googleMap
        mMapGoogle.clear()
        if (location.lat != 52.245696 && location.lng != -7.139102) {
            val loc = LatLng(location.lat, location.lng)
            val options = MarkerOptions()
                .title("HillForts")
                .snippet("GPS : " + loc.toString())
                .position(loc)
            mMapGoogle.addMarker(options)
            mMapGoogle.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
        } else if (hillfort.location.lat != 0.0 && hillfort.location.lng != 0.0) {
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


    fun doAddFort(
        date: String,
        hillfort: HillFortModel
    ) {
        hillfort.name = view.mHillFortName.text.toString()
        hillfort.description = view.mHillFortDescription.text.toString()
        hillfort.location = location
        hillfort.visitCheck = view.mHillFortVisitedCheckbox.isChecked
        if(view.mHillFortAddDate.isChecked) {
            hillfort.datevisted = date
        }

        if (hillfort.name.isNotEmpty() && hillfort.imageStore.isNotEmpty()){
            if(editinghillfort){
                app.hillforts.updateHillforts(hillfort.copy(),user)
            }else{
                app.hillforts.createHillfort(hillfort.copy(),user)
            }
            view.info { "add Button Pressed: ${hillfort}" }
            view.setResult(Activity.RESULT_OK)
            view.finish()
        } else {
            view.toast(view.getString(R.string.addfort_entertitleandimage))
        }
    }

    //  When a result comes back
    fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent?, addFortActivity: AddFortView, hillfort: HillFortModel,context: Context) {
        when(requestCode){
            IMAGE_REQUEST -> {
                if (data != null){
                    hillfort.image = data.data.toString()
                    hillfort.imageStore.add(hillfort.image)
                    val viewPager = view.findViewById<ViewPager>(R.id.mAddFortImagePager)
                    val adapter = ImageAdapter(
                        context,
                        hillfort.imageStore
                    )
                    viewPager.adapter = adapter
                }
            }
            LOCATION_REQUEST -> {
            if (data != null) {
                location = data.extras?.getParcelable<Location>("location")!!
                view.mHillFortLocationText.text = location.toString()
                val mMap = (view.supportFragmentManager.findFragmentById(R.id.mMapFragment) as SupportMapFragment)
                mMap.getMapAsync(addFortActivity)
            }
        }
        }
    }
}