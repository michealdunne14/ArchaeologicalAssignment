package com.example.archaeologicalfieldwork.activities.AddFort

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseActivity.BasePresenter
import com.example.archaeologicalfieldwork.activities.BaseActivity.BaseView
import com.example.archaeologicalfieldwork.activities.BaseActivity.VIEW
import com.example.archaeologicalfieldwork.activities.Database.HillfortFireStore
import com.example.archaeologicalfieldwork.helper.checkLocationPermissions
import com.example.archaeologicalfieldwork.helper.isPermissionGranted
import com.example.archaeologicalfieldwork.helper.showImagePicker
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.*
import com.example.archaeologicalfieldwork.models.jsonstore.generateRandomId
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_addfort.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.text.ParseException
import java.text.SimpleDateFormat

class FortPresenter(view: BaseView):
    BasePresenter(view){
    var user = UserModel()
    var hillforts = HillFortModel()
    var location = Location(0,52.245696, -7.139102, 15f)
    var defaultLocation = Location(0,52.245696, -7.139102, 15f)
    override var app : MainApp = view.application as MainApp
    var map: GoogleMap? = null

    var listofImages = ArrayList<String>()

    var editinghillfort = false

    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)

    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2

    var fireStore: HillfortFireStore? = null

    init {
        if (app.hillforts is HillfortFireStore) {
            fireStore = app.hillforts as HillfortFireStore
        }
        user = fireStore!!.findCurrentUser()
        if (view.intent.hasExtra("hillfort_edit")){
            editinghillfort = true
            hillforts = view.intent.extras?.getParcelable("hillfort_edit")!!
            listofImages = view.intent.extras?.getStringArrayList("images") as ArrayList<String>
            view.putHillfort(hillforts)
            view.addImages(listofImages)
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
        doAsync {
            fireStore?.deleteHillforts(hillforts, user)
            uiThread {
                view.finish()
            }
        }
    }

    fun doSelectImage(){
        showImagePicker(view,IMAGE_REQUEST)
    }

    fun doRemoveImage(currentItem: Int,hillfort: HillFortModel) {

//        app.hillforts.findHillfortImages(currentItem)
    }

    fun doEditHillfort(hillfort: HillFortModel) {
        view.putHillfort(hillfort)
        view.addImages(listofImages)
        findNotes()
//          Formatting date to long to pass in to calender
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        try {
            val date = formatter.parse(hillfort.datevisted)
            val dateInLong = date.time
            view.mHillFortDatePicker.date = dateInLong
        } catch (e: ParseException) {
            e.printStackTrace()
        }

//          View Pager for multiple images
        editinghillfort = true
        view.showHillfortAdd()
    }


    fun doAddFort(date: String, hillfort: HillFortModel) {
        if(view.mHillFortAddDate.isChecked) {
            hillfort.datevisted = date
        }
        hillfort.id = generateRandomId()
        if (hillfort.name.isNotEmpty() && listofImages.size > 0){
            if(editinghillfort){
                doAsync {
                    hillfort.fbId = hillforts.fbId
                    fireStore?.updateHillforts(hillfort.copy())
                    fireStore?.updateImage(listofImages,hillfort.fbId)
                    uiThread {
                        view.navigateTo(VIEW.LIST)
                    }
                }
            }else{
                doAsync {
                    fireStore?.createHillfort(hillfort.copy(),user,listofImages)
                    uiThread {
                        view.navigateTo(VIEW.LIST)
                    }
                }
            }
            view.showResult(hillfort)
        } else {
            view.toast(view.getString(R.string.addfort_entertitleandimage))
        }
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(location.lat,location.lng)
    }

    fun doSetLocation() {
        view.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(0,location.lat, location.lng, location.zoom))
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
        location.lat = lat
        location.lng = lng
        location.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(hillforts.name).position(LatLng(location.lat, location.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.lat, location.lng), location.zoom))
        view.putHillfort(hillforts)
    }

    //  When a result comes back
    fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent?, addFortActivity: AddFortView, hillfort: HillFortModel,context: Context) {
        when(requestCode){
            IMAGE_REQUEST -> {
                if (data != null){
                    listofImages.add(data.data.toString())
                    view.addImages(listofImages)
                }
            }
            LOCATION_REQUEST -> {
                val location = data?.extras?.getParcelable<Location>("location")!!
                location.lat = location.lat
                location.lng = location.lng
                location.zoom = location.zoom
                locationUpdate(location.lat, location.lng)
                hillfort.location = location
                view.showLocation(hillfort,location)
            }
        }
    }


    fun findNotes() {
        doAsync {
            val notes = fireStore!!.findNotes(hillforts)
            uiThread {
                view.showNotes(notes)
            }
        }
    }
}