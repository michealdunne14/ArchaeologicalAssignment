package com.example.archaeologicalfieldwork.activities.Main

import com.example.archaeologicalfieldwork.activities.BaseActivity.BasePresenter
import com.example.archaeologicalfieldwork.activities.BaseActivity.VIEW
import com.example.archaeologicalfieldwork.activities.Database.HillfortFireStore
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Images
import com.example.archaeologicalfieldwork.models.UserModel

class MainPresenter(view: MainView): BasePresenter(view){

    var user = UserModel()
    override var app : MainApp = view.application as MainApp
    var fireStore: HillfortFireStore? = null


    init {
        if (app.hillforts is HillfortFireStore) {
            fireStore = app.hillforts as HillfortFireStore
        }
    }


    fun getHillforts(): List<HillFortModel> = fireStore!!.findAllHillforts(user)

    fun getImages(fbId: String): List<Images> = fireStore!!.findImages(fbId)

    fun doAddHillfort(){
        view.navigateTo(VIEW.HILLFORT)
    }

    fun doEditHillfort(hillFortModel: HillFortModel){
        view.navigateTo(VIEW.HILLFORT,0,"hillfort_edit",hillFortModel)
    }

    fun doShowHillfortMap(){
        view.navigateTo(VIEW.MAPS)
    }

}