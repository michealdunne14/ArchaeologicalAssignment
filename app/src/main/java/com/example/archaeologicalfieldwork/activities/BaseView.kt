package com.example.archaeologicalfieldwork.activities

import android.content.Intent
import android.os.Parcelable

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.archaeologicalfieldwork.activities.AddFort.AddFortView
import com.example.archaeologicalfieldwork.activities.EditLocation.EditLocationView
import com.example.archaeologicalfieldwork.activities.Main.MainView
import com.example.archaeologicalfieldwork.activities.Maps.HillfortMapsView
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Location
import org.jetbrains.anko.AnkoLogger

val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2

enum class VIEW {
    LOCATION,HILLFORT,MAPS,LIST
}

open abstract class BaseView: AppCompatActivity(),AnkoLogger{


        var basePresenter: BasePresenter? = null

        fun navigateTo(view: VIEW,code:Int = 0,key:String = "",value: Parcelable? = null){
            var intent = Intent(this,MainView::class.java)
            when(view){
                VIEW.LOCATION -> intent = Intent(this,EditLocationView::class.java)
                VIEW.HILLFORT -> intent = Intent(this,AddFortView::class.java)
                VIEW.MAPS -> intent = Intent(this,HillfortMapsView::class.java)
                VIEW.LIST -> intent = Intent(this,MainView::class.java)
            }
            if (key != ""){
                intent.putExtra(key,value)
            }
            startActivityForResult(intent,code)
        }

        fun initPresenter(presenter: BasePresenter):BasePresenter{
            basePresenter = presenter
            return presenter
        }

        fun init(toolbar: Toolbar){
            toolbar.title = title
            setSupportActionBar(toolbar)
        }

        override fun onDestroy() {
            basePresenter?.onDestrop()
            super.onDestroy()
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (data != null) {
                basePresenter?.doActivityResult(requestCode,resultCode,data)
            }
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
        }

        open fun showHillfort(hillFortModel: HillFortModel){}
        open fun showResult(hillFortModel: HillFortModel){}
        open fun showLocation(hillFortModel: HillFortModel, location: Location){}
        open fun showImages(){}
        open fun showHillfortAdd(){}

}