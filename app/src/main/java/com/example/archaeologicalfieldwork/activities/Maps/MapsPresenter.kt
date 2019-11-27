package com.example.archaeologicalfieldwork.activities.Maps

import com.example.archaeologicalfieldwork.activities.BasePresenter
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.content_hillfort_maps.*

class MapsPresenter(view: HillfortMapsView): BasePresenter(view) {

    override lateinit var app: MainApp

    init {
        app = view.application as MainApp
    }

    fun initMap(map: GoogleMap) {
        map.uiSettings.isZoomControlsEnabled = true
        app.hillforts.findAllHillforts(app.user).forEach {
            val loc = LatLng(it.location.lat, it.location.lng)
            val options = MarkerOptions().title(it.name).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
        }
    }

    fun doMarkerClick(marker: Marker) {
        view.currentTitle.text = marker.title
        var hillFortModel: HillFortModel = app.hillforts.findHillfort(app.user,marker.tag.toString().toLong())!!
        view.currentDescription.text = hillFortModel.description
    }
}