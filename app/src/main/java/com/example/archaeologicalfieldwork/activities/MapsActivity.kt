package com.example.archaeologicalfieldwork.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.models.Location

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(),GoogleMap.OnMarkerDragListener,GoogleMap.OnMarkerClickListener {

    lateinit var map: GoogleMap
    lateinit var mapsPresenter: MapsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
//      Gets the location from the add fort activity

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapsPresenter = MapsPresenter(this)
        mapFragment.getMapAsync {
            map = it
            map.setOnMarkerClickListener(this)
            map.setOnMarkerDragListener(this)
            mapsPresenter.initMap(map)
        }
        toolbarMap.title = title
        setSupportActionBar(toolbarMap)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val resultIntent = Intent()
        resultIntent.putExtra("location", mapsPresenter.doGetLocation())
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        mapsPresenter.doOnBackPressed()
    }


    override fun onMarkerDragEnd(marker: Marker) {
        mapsPresenter.doUpdateLocation(marker.position.latitude,marker.position.longitude,map.cameraPosition.zoom)
    }

    override fun onMarkerDragStart(p0: Marker?) {}

    override fun onMarkerDrag(p0: Marker?) {}

    override fun onMarkerClick(marker: Marker): Boolean {
        mapsPresenter.doUpdateMarker(marker)
        return false
    }



}
