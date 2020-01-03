package com.example.archaeologicalfieldwork.activities.EditLocation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseActivity.BaseView

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_maps.*
import org.jetbrains.anko.info

class EditLocationView : BaseView(),GoogleMap.OnMarkerDragListener,GoogleMap.OnMarkerClickListener {

    lateinit var map: GoogleMap
    lateinit var editLocationPresenter: EditLocationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        info { "Edit Location Started" }

//      Gets the location from the add fort activity
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        editLocationPresenter = initPresenter(EditLocationPresenter(this)) as EditLocationPresenter
        mapFragment.getMapAsync {
            map = it
            map.setOnMarkerClickListener(this)
            map.setOnMarkerDragListener(this)
            editLocationPresenter.initMap(map)
        }
//      Sets up toolbar
        toolbarMap.title = title
        setSupportActionBar(toolbarMap)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    //Set the location in text
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val resultIntent = Intent()
        resultIntent.putExtra("location", editLocationPresenter.doGetLocation())
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        return super.onOptionsItemSelected(item)
    }
//  Back button pressed
    override fun onBackPressed() {
        editLocationPresenter.doOnBackPressed()
    }


    override fun onMarkerDragEnd(marker: Marker) {
        editLocationPresenter.doUpdateLocation(marker.position.latitude,marker.position.longitude,map.cameraPosition.zoom)
    }

    override fun onMarkerDragStart(p0: Marker?) {}

    override fun onMarkerDrag(p0: Marker?) {}

    override fun onMarkerClick(marker: Marker): Boolean {
        editLocationPresenter.doUpdateMarker(marker)
        return false
    }



}
