package com.example.archaeologicalfieldwork.activities.Maps

import com.example.archaeologicalfieldwork.activities.BaseActivity.BasePresenter
import com.example.archaeologicalfieldwork.activities.Database.HillfortFireStore
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Images
import com.example.archaeologicalfieldwork.models.UserModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MapsPresenter(view: HillfortMapsView): BasePresenter(view) {

    override var app: MainApp = view.application as MainApp
    lateinit var currentUser: UserModel
    var fireStore: HillfortFireStore? = null


    init {
        if (app.hillforts is HillfortFireStore) {
            fireStore = app.hillforts as HillfortFireStore
            currentUser = fireStore!!.currentUser()
        }
    }

    fun initMap(map: GoogleMap) {
        map.uiSettings.isZoomControlsEnabled = true
        findHillforts(map)
    }
//  Get Hillfort Images
    fun getImages(): List<Images> = fireStore!!.getImages()

    fun doMarkerClick(marker: String) {
        val hillFortModel: HillFortModel = fireStore!!.findHillfort(marker)!!
        val images = getImages()
        val searchedImages = ArrayList<Images>()
        for (i in images){
            if (i.hillfortFbid == hillFortModel.fbId){
                searchedImages.add(i)
            }
        }
        view.setMarkerDetails(searchedImages,hillFortModel)
    }

    fun findHillforts(map: GoogleMap) {
        val findHillforts = fireStore!!.findAllHillforts()
        findHillforts.forEach {
            val loc = LatLng(it.location.lat, it.location.lng)
            val options = MarkerOptions().title(it.name).position(loc)
            map.addMarker(options).tag = it
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
        }
    }
}