package com.example.archaeologicalfieldwork.activities

import android.content.Intent
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel

open class BasePresenter(var view: BaseView) {
    open var app:MainApp = view.application as MainApp

    open fun doActivityResult(requestCode: Int,resultCode:Int,data: Intent){

    }

    open fun doRequestPermissionsResult(requestCode: Int,permissions: Array<String>,grantResult: IntArray){

    }

    open fun onDestrop(){

    }

}