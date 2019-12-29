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
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class FortPresenter(view: BaseView):
    BasePresenter(view){
    var user = UserModel()
    var hillforts = HillFortModel()
    var location = Location(0,52.245696, -7.139102, 15f)
    var defaultLocation = Location(0,52.245696, -7.139102, 15f)
    override var app : MainApp = view.application as MainApp
    var map: GoogleMap? = null

    var listofImages = ArrayList<Images>()
    var listofNotes= ArrayList<Notes>()
    val stringList = ArrayList<String>()

    var editinghillfort = false

    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)

    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2

    var fireStore: HillfortFireStore? = null

    init {
//        Set up firebase and get current user
        if (app.hillforts is HillfortFireStore) {
            fireStore = app.hillforts as HillfortFireStore
            user = fireStore!!.currentUser()
        }
//      If hillfort is being edited
        if (view.intent.hasExtra("hillfort_edit")){
            editinghillfort = true
            hillforts = view.intent.extras?.getParcelable("hillfort_edit")!!
            listofImages = view.intent.extras?.getParcelableArrayList<Images>("images")!!
            listofNotes = fireStore!!.getArrayListofNotes()
            for (i in listofImages){
                if (i.hillfortFbid == hillforts.fbId) {
                    stringList.add(i.image)
                }
            }
//          Input data in to each field
            view.showLocation(hillforts.location)
            view.showNotes(listofNotes)
            view.putHillfort(hillforts)
            view.addImages(stringList)
            view.showHillfortUpdate()
        }else{
//            Set current Location
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
        }
    }

    fun doDelete() {
//      Delete Hillfort
        doAsync {
            fireStore?.deleteHillforts(hillforts, user)
            uiThread {
                view.finish()
            }
        }
    }

    fun doSelectImage(){
//      Select Images
        showImagePicker(view,IMAGE_REQUEST)
    }

    fun doRemoveImage(currentItem: Int) {
//      Remove Image
        fireStore?.deleteImage(listofImages,currentItem,stringList)

    }
//  Add or save Hillfort Details
    fun doAddFort(hillfort: HillFortModel) {
        hillfort.id = generateRandomId()
        if (hillfort.name.isNotEmpty() && stringList.size > 0){
            if(editinghillfort){
                doAsync {
                    hillfort.fbId = hillforts.fbId
                    hillfort.location = location
                    hillforts.starCheck = hillfort.starCheck
                    hillforts.visitCheck = hillfort.visitCheck
                    fireStore?.updateHillforts(hillfort.copy())
                    fireStore?.updateImage(stringList,hillfort.fbId)
                    uiThread {
                        view.navigateTo(VIEW.LIST)
                    }
                }
            }else{
                doAsync {
                    hillfort.location = location
                    fireStore?.createHillfort(hillfort.copy(),user,stringList)
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
//  Configure map
    fun doConfigureMap(m: GoogleMap) {
        map = m
    }
//  Set location
    fun doSetLocation() {
        view.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(0,location.lat, location.lng, location.zoom))
    }
//  Update Location to current location
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
            view.showLocation(location)
        }
    }


    fun updateLocation() {
        locationUpdate(hillforts.location.lat,hillforts.location.lng)
    }
//  Set the location of the map
    fun locationUpdate(lat: Double, lng: Double) {
        location.lat = lat
        location.lng = lng
        location.zoom = 15f
        hillforts.location = location
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(hillforts.name).position(LatLng(location.lat, location.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.lat, location.lng), location.zoom))
    }

    //  When a result comes back
    fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent?, addFortActivity: AddFortView, hillfort: HillFortModel,context: Context) {
        when(requestCode){
            IMAGE_REQUEST -> {
                if (data != null){
                    stringList.add(data.data.toString())
                    view.addImages(stringList)
                }
            }
            LOCATION_REQUEST -> {
                val location = data?.extras?.getParcelable<Location>("location")!!
                location.lat = location.lat
                location.lng = location.lng
                location.zoom = location.zoom
                locationUpdate(location.lat, location.lng)
                hillfort.location = location
                view.showLocation(location)
            }
        }
    }
}