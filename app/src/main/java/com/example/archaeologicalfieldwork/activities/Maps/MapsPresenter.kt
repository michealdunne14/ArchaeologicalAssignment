package com.example.archaeologicalfieldwork.activities.Maps

import com.example.archaeologicalfieldwork.activities.BaseActivity.BasePresenter
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.UserModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.content_hillfort_maps.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MapsPresenter(view: HillfortMapsView): BasePresenter(view) {

    override var app: MainApp = view.application as MainApp
    lateinit var currentUser: UserModel

    fun initMap(map: GoogleMap) {
        map.uiSettings.isZoomControlsEnabled = true
        doAsync {
            currentUser = app.hillforts.findCurrentUser()
            uiThread {
                findHillforts(currentUser,map)
            }
        }
    }

    fun doMarkerClick(marker: Marker) {
        doAsync {
            val hillFortModel: HillFortModel = app.hillforts.findHillfort(currentUser, marker.tag.toString().toLong())!!
            uiThread {
                view.currentDescription.text = hillFortModel.description
                view.currentTitle.text = hillFortModel.name
            }
        }
    }

    fun findHillforts(
        currentUser: UserModel,
        map: GoogleMap
    ) {
        doAsync {
            val findHillforts = app.hillforts.findAllHillforts(currentUser)
            uiThread {
                findHillforts.forEach {
                    val loc = LatLng(it.location.lat, it.location.lng)
                    val options = MarkerOptions().title(it.name).position(loc)
                    map.addMarker(options).tag = it
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
                }
            }
        }
    }
}