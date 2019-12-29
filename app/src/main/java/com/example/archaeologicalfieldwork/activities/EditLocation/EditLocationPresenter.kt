package com.example.archaeologicalfieldwork.activities.EditLocation

import android.app.Activity
import android.content.Intent
import com.example.archaeologicalfieldwork.activities.BaseActivity.BasePresenter
import com.example.archaeologicalfieldwork.activities.BaseActivity.BaseView
import com.example.archaeologicalfieldwork.models.Location
import com.example.archaeologicalfieldwork.models.jsonstore.generateRandomId
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class EditLocationPresenter(view: BaseView): BasePresenter(view){

    var location = Location()

    init {
        location = view.intent.extras?.getParcelable<Location>("location")!!
    }
//  Create map add marker
    fun initMap(googleMap: GoogleMap) {
        val loc = LatLng(location.lat, location.lng)
        val options = MarkerOptions()
            .title("HillForts")
            .snippet("GPS : " + loc.toString())
            .draggable(true)
            .position(loc)
        googleMap.addMarker(options)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
    }

//    Update Location
    fun doUpdateLocation(lat: Double, lng: Double,zoom: Float){
        location.locationid = generateRandomId()
        location.lat = lat
        location.lng = lng
        location.zoom = zoom
    }
//  Set location when back button is pressed
    fun doOnBackPressed(){
        val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        view.setResult(Activity.RESULT_OK, resultIntent)
        view.finish()
    }
//  Get Location
    fun doGetLocation(): Location {
        return location
    }
//  Update the marker
    fun doUpdateMarker(marker: Marker){
        val loc = LatLng(location.lat,location.lng)
        marker.snippet = "GPS: " + loc.toString()
    }
}