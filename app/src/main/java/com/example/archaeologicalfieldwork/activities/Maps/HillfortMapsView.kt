package com.example.archaeologicalfieldwork.activities.Maps

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseActivity.BaseView
import com.example.archaeologicalfieldwork.adapter.ImageAdapter
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Images
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

import kotlinx.android.synthetic.main.content_hillfort_maps.*
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class HillfortMapsView : BaseView(),GoogleMap.OnMarkerClickListener {

    lateinit var map: GoogleMap
    lateinit var mapsPresenter: MapsPresenter
    lateinit var context: Context
    var hillfort = HillFortModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_maps)
        mapView.onCreate(savedInstanceState)
        context = mapView.context
        info { "Maps Started" }
//      Sets up the map
        mapsPresenter = initPresenter(MapsPresenter(this)) as MapsPresenter
        mapView.getMapAsync {
            map = it
            map.setOnMarkerClickListener(this)
            mapsPresenter.initMap(map)
        }
        mNaviagationButton.setOnClickListener {
            if (hillfort.name.isNotEmpty()) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("geo:0,0?q=${hillfort.location.lat},${hillfort.location.lng} (${android.R.attr.name})")
                )
                startActivity(intent)
            }else{
                toast("No marker selected")
            }
        }
    }
//  Set the marker details
override fun setMarkerDetails(images: List<Images>,hillFortModel: HillFortModel) {
    hillfort = hillFortModel
    val imageArrayList = ArrayList<String>()
    for (i in images){
        imageArrayList.add(i.image)
    }
    val viewPager = findViewById<ViewPager>(R.id.mViewPagerImages)
    val adapter = ImageAdapter(context, imageArrayList)
    viewPager.adapter = adapter
    currentDescription.text = hillFortModel.description
    currentTitle.text = hillFortModel.name
}

//  Show details of the marker clicked
    override fun onMarkerClick(marker: Marker): Boolean {
        mapsPresenter.doMarkerClick(marker.title)
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
