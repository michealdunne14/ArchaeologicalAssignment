package com.example.archaeologicalfieldwork.activities.BaseActivity

import android.content.Intent
import android.os.Parcelable

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.AddFort.AddFortView
import com.example.archaeologicalfieldwork.activities.EditLocation.EditLocationView
import com.example.archaeologicalfieldwork.activities.Login.LoginView
import com.example.archaeologicalfieldwork.activities.Main.MainView
import com.example.archaeologicalfieldwork.activities.Maps.HillfortMapsView
import com.example.archaeologicalfieldwork.activities.Register.RegisterView
import com.example.archaeologicalfieldwork.activities.StartActivity
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Images
import com.example.archaeologicalfieldwork.models.Location
import com.example.archaeologicalfieldwork.models.Notes
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger

val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2

enum class VIEW {
    LOCATION,HILLFORT,MAPS,LIST,LOGIN,START,REGISTER
}

open abstract class BaseView: AppCompatActivity(),AnkoLogger{

    var basePresenter: BasePresenter? = null
//  Do on start up
    fun init(toolbar: Toolbar, upEnabled: Boolean) {
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            toolbar.title = "${title}: ${user.email}"
        }
    }
//  Easily navigate threw the application
    fun navigateTo(view: VIEW, code:Int = 0, key:String = "", value: Parcelable? = null){
        var intent = Intent(this,MainView::class.java)
        when(view){
            VIEW.LOCATION -> intent = Intent(this,EditLocationView::class.java)
            VIEW.HILLFORT -> intent = Intent(this,AddFortView::class.java)
            VIEW.MAPS -> intent = Intent(this,HillfortMapsView::class.java)
            VIEW.LIST -> intent = Intent(this,MainView::class.java)
            VIEW.LOGIN -> intent = Intent(this,LoginView::class.java)
            VIEW.REGISTER -> intent = Intent(this,RegisterView::class.java)
            VIEW.START -> intent = Intent(this, StartActivity::class.java)
        }
        if (key != ""){
            intent.putExtra(key,value)
        }
        startActivityForResult(intent,code)
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
    }
//  Set Presenter
    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
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

    open fun putHillfort(hillFortModel: HillFortModel){}
    open fun addImages(listofImages: ArrayList<String>) {}
    open fun showResult(hillFortModel: HillFortModel){}
    open fun showLocation(location: Location){}
    open fun showHillfortUpdate(){}
    open fun showProgress(){}
    open fun hideProgress(){}
    open fun showNotes(notes: List<Notes>) {}
    open fun setMarkerDetails(images: List<Images>,hillFortModel: HillFortModel) {}
}