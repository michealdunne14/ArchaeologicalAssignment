package com.example.archaeologicalfieldwork.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.models.Location

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,GoogleMap.OnMarkerDragListener,GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    var location = Location()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        location = intent.extras?.getParcelable<Location>("location")!!
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.setOnMarkerClickListener(this)
        val loc = LatLng(location.lat, location.lng)
        mMap.setOnMarkerDragListener(this)
        val options = MarkerOptions()
            .title("HillForts")
            .snippet("GPS : " + loc.toString())
            .draggable(true)
            .position(loc)
        mMap.addMarker(options)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
    }

    override fun onBackPressed() {
        val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        super.onBackPressed()
    }


    override fun onMarkerDragEnd(marker: Marker) {
        location.lat = marker.position.latitude
        location.lng = marker.position.longitude
        location.zoom = mMap.cameraPosition.zoom
    }

    override fun onMarkerDragStart(p0: Marker?) {
    }

    override fun onMarkerDrag(p0: Marker?) {
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        location.lat = marker.position.latitude
        location.lng = marker.position.longitude
        marker.snippet = "GPS: lat/lng:("+ location.lat +","+ location.lng +")"
        return false
    }



}
