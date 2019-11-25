package com.example.archaeologicalfieldwork.activities.Maps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker

import kotlinx.android.synthetic.main.activity_hillfort_maps.*
import kotlinx.android.synthetic.main.content_hillfort_maps.*

class HillfortMapsActivity : BaseView(),GoogleMap.OnMarkerClickListener {

    lateinit var map: GoogleMap
    lateinit var mapsPresenter: MapsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_maps)
        setSupportActionBar(toolbar)

        toolbar.title = title
        setSupportActionBar(toolbar)
        mapView.onCreate(savedInstanceState)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapsPresenter = initPresenter(MapsPresenter(this)) as MapsPresenter
        mapFragment.getMapAsync {
            map = it
            map.setOnMarkerClickListener(this)
            mapsPresenter.initMap(map)
        }
    }

//  heksefd
    override fun onMarkerClick(marker: Marker): Boolean {
        mapsPresenter.doMarkerClick(marker)
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
