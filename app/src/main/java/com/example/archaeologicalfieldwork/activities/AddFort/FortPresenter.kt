package com.example.archaeologicalfieldwork.activities.AddFort

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BasePresenter
import com.example.archaeologicalfieldwork.activities.BaseView
import com.example.archaeologicalfieldwork.activities.EditLocation.EditLocationView
import com.example.archaeologicalfieldwork.activities.VIEW
import com.example.archaeologicalfieldwork.adapter.ImageAdapter
import com.example.archaeologicalfieldwork.helper.checkLocationPermissions
import com.example.archaeologicalfieldwork.helper.isPermissionGranted
import com.example.archaeologicalfieldwork.helper.showImagePicker
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Location
import com.example.archaeologicalfieldwork.models.UserModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
    var defaultLocation = Location(52.245696, -7.139102, 15f)
    override var app : MainApp = view.application as MainApp
    var map: GoogleMap? = null

    var editinghillfort = false

    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)


    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2

    init {
        user = app.user
        if (view.intent.hasExtra("hillfort_edit")){
            editinghillfort = true
            hillfort = view.intent.extras?.getParcelable<HillFortModel>("hillfort_edit")!!
            view.showHillfort(hillfort)
        }else{
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
        }
    }

    fun doCancel() {
        view.finish()
    }

    fun doDelete() {
        app.hillforts.deleteHillforts(hillfort,user)
        view.finish()
    }

    fun doSelectImage(){
        showImagePicker(view,IMAGE_REQUEST)
    }

    fun doRemoveImage(currentItem: Int,hillfort: HillFortModel) {
        if(hillfort.imageStore.size == 0){
            view.toast(view.getString(R.string.deleteimages))
        }else {
            hillfort.imageStore.removeAt(currentItem)
            view.showImages()
        }
    }

    fun doEditHillfort(hillfort: HillFortModel) {
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
        view.showImages()
        editinghillfort = true
        view.showHillfortAdd()
    }


    fun doAddFort(date: String, hillfort: HillFortModel) {
        view.showHillfort(hillfort)
        hillfort.location = location
        if(view.mHillFortAddDate.isChecked) {
            hillfort.datevisted = date
        }

        if (hillfort.name.isNotEmpty() && hillfort.imageStore.isNotEmpty()){
            if(editinghillfort){
                app.hillforts.updateHillforts(hillfort.copy(),user)
            }else{
                app.hillforts.createHillfort(hillfort.copy(),user)
            }
            view.showResult(hillfort)
        } else {
            view.toast(view.getString(R.string.addfort_entertitleandimage))
        }
    }


    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(hillfort.location.lat, hillfort.location.lng)
    }

    fun doSetLocation() {
        view.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(hillfort.location.lat, hillfort.location.lng, hillfort.location.zoom))
    }

    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            // permissions denied, so use the default location
            locationUpdate(defaultLocation.lat, defaultLocation.lng)
        }
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    fun locationUpdate(lat: Double, lng: Double) {
        hillfort.location.lat = lat
        hillfort.location.lng = lng
        hillfort.location.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(hillfort.name).position(LatLng(hillfort.location.lat, hillfort.location.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(hillfort.location.lat, hillfort.location.lng), hillfort.location.zoom))
        view.showHillfort(hillfort)
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
                val location = data?.extras?.getParcelable<Location>("location")!!
                hillfort.location.lat = location.lat
                hillfort.location.lng = location.lng
                hillfort.location.zoom = location.zoom
                locationUpdate(hillfort.location.lat, hillfort.location.lng)
            }
        }
    }
}