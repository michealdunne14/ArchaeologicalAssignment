package com.example.archaeologicalfieldwork.fragment

import com.example.archaeologicalfieldwork.activities.BaseFragment.BaseFragmentPresenter
import com.example.archaeologicalfieldwork.activities.Database.HillfortFireStore
import com.example.archaeologicalfieldwork.main.MainApp
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
            user = fireStore!!.currentUser()
        }
    }

    fun clearHillforts(){
        fireStore?.clearSearchResult()
    }

    fun findNotes(fbId: String) {
        val clearnotes = fireStore!!.getArrayListofNotes()
        clearnotes.clear()
        fireStore!!.findNotes(fbId)
    }

    fun findallHillforts() {
        doAsync {
            val searchedHillforts = fireStore?.findSearchedHillforts()
            val hillfort = fireStore!!.findAllHillforts()
            uiThread {
                if (searchedHillforts != null) {
                    if (searchedHillforts.isNotEmpty()) {
                        view.showFloatingAction()
                        val images = fireStore!!.getImages()
                        view.showHillforts(searchedHillforts, user, images)
                    } else {
                        view.hideFloatingAction()
                        val images = fireStore!!.getImages()
                        view.showHillforts(hillfort, user, images)
                    }
                }
            }
        }
    }
}