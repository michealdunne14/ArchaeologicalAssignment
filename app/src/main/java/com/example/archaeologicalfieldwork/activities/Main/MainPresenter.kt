package com.example.archaeologicalfieldwork.activities.Main

import com.example.archaeologicalfieldwork.activities.BasePresenter
import com.example.archaeologicalfieldwork.activities.VIEW
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.UserModel

class MainPresenter(view: MainView): BasePresenter(view){

    var user : UserModel
    override var app : MainApp = view.application as MainApp

    init {
        user = app.user
    }


    fun getHillforts(): List<HillFortModel> = app.hillforts.findAllHillforts(user)

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