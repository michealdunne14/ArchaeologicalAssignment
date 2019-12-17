package com.example.archaeologicalfieldwork.fragment

import com.example.archaeologicalfieldwork.activities.BaseFragment.BaseFragmentPresenter
import com.example.archaeologicalfieldwork.activities.Database.HillfortFireStore
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.Notes
import com.example.archaeologicalfieldwork.models.UserModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class HomeFragPresenter(view: HomeFragView): BaseFragmentPresenter(view) {
    var user = UserModel()
    override var app : MainApp = view.activity?.application as MainApp
    var fireStore: HillfortFireStore? = null

    init {
        if (app.hillforts is HillfortFireStore) {
            fireStore = app.hillforts as HillfortFireStore
        }
        user = app.hillforts.findCurrentUser()
    }

    fun findallHillforts() {
        doAsync {
            val hillfort = fireStore!!.findAllHillforts(user)
            uiThread {
                view.showHillforts(hillfort,user)
            }
        }
    }
}