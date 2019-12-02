package com.example.archaeologicalfieldwork.activities.BaseFragment

import android.content.Intent
import com.example.archaeologicalfieldwork.fragment.HomeFragView
import com.example.archaeologicalfieldwork.main.MainApp

open class BaseFragmentPresenter(var view: BaseFragmentView) {
    open var app: MainApp = view.activity?.application as MainApp

    open fun doActivityResult(requestCode: Int,resultCode:Int,data: Intent){
    }

    open fun doRequestPermissionsResult(requestCode: Int,permissions: Array<String>,grantResult: IntArray){

    }

    open fun onDestrop(){

    }
}