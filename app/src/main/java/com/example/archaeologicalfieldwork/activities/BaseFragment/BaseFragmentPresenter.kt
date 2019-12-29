package com.example.archaeologicalfieldwork.activities.BaseFragment

import android.content.Intent
import com.example.archaeologicalfieldwork.activities.Database.HillfortFireStore
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Images

open class BaseFragmentPresenter(var view: BaseFragmentView) {
    open var app: MainApp = view.activity?.application as MainApp

    open fun doActivityResult(requestCode: Int,resultCode:Int,data: Intent){
    }

    open fun doRequestPermissionsResult(requestCode: Int,permissions: Array<String>,grantResult: IntArray){

    }

    open fun onDestrop(){

    }

    open fun doCreateNote(fireStore: HillfortFireStore?, note: String, hillfort: HillFortModel){}
    open fun doLikeUpdateHillforts(hillfort: HillFortModel){}
    open fun doStarUpdateHillforts(hillfort: HillFortModel){}
}