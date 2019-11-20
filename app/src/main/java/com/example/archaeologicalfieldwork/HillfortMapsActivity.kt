package com.example.archaeologicalfieldwork

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

import kotlinx.android.synthetic.main.activity_hillfort_maps.*
import kotlinx.android.synthetic.main.content_hillfort_maps.*

class HillfortMapsActivity : AppCompatActivity(),GoogleMap.OnMarkerClickListener {

    lateinit var map : GoogleMap
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_maps)
        setSupportActionBar(toolbar)

        app = application as MainApp

        toolbar.title = title
        setSupportActionBar(toolbar)
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync{
            map = it
            configureMap()
        }

    }

    fun configureMap(){
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        app.hillforts.findAllHillforts(app.user).forEach {
            val loc = LatLng(it.location.lat, it.location.lng)
            val options = MarkerOptions().title(it.name).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
        }
    }
//  heksefd
    override fun onMarkerClick(marker: Marker): Boolean {
        currentTitle.text = marker.title
        var hillFortModel: HillFortModel = app.hillforts.findHillfort(app.user,marker.tag.toString().toLong())!!
        currentDescription.text = hillFortModel.description
        return false
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
