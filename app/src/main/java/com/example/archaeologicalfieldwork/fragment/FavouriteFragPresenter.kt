package com.example.archaeologicalfieldwork.fragment

import com.example.archaeologicalfieldwork.activities.BaseFragment.BaseFragmentPresenter
import com.example.archaeologicalfieldwork.activities.Database.HillfortFireStore
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.UserModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class FavouriteFragPresenter(view: FavouriteFragView): BaseFragmentPresenter(view) {
    var user = UserModel()
    override var app : MainApp = view.activity?.application as MainApp
    var fireStore: HillfortFireStore? = null

    init {
        user = app.hillforts.findCurrentUser()
        if (app.hillforts is HillfortFireStore) {
            fireStore = app.hillforts as HillfortFireStore
        }
    }

    fun findallHillforts() {
        doAsync {
            val hillfort = fireStore?.findHillfortsWithStar(user)
            uiThread {
                //                view.showHillforts(hillfort as List<HillFortModel>,user)
            }
        }
    }

}