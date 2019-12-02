package com.example.archaeologicalfieldwork.activities.Maps

import android.os.Bundle
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseActivity.BaseView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

import kotlinx.android.synthetic.main.activity_hillfort_maps.*
import kotlinx.android.synthetic.main.content_hillfort_maps.*

class HillfortMapsView : BaseView(),GoogleMap.OnMarkerClickListener {

    lateinit var map: GoogleMap
    lateinit var mapsPresenter: MapsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_maps)
        setSupportActionBar(toolbar)
        toolbar.title = title
        setSupportActionBar(toolbar)
        mapView.onCreate(savedInstanceState)

        mapsPresenter = initPresenter(MapsPresenter(this)) as MapsPresenter
        mapView.getMapAsync {
            map = it
            map.setOnMarkerClickListener(this)
            mapsPresenter.initMap(map)
        }
    }


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