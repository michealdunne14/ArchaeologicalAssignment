package com.example.archaeologicalfieldwork.fragment

import androidx.viewpager.widget.ViewPager
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseFragment.BaseFragmentPresenter
import com.example.archaeologicalfieldwork.activities.Database.HillfortFireStore
import com.example.archaeologicalfieldwork.adapter.ImageAdapter
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Images
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
//  Clear Search hillforts
    fun clearSearchHillforts(){
        fireStore?.clearSearchResult()
    }

//  Get notes
    fun findNotes(fbId: String) {
        val clearnotes = fireStore!!.getArrayListofNotes()
        clearnotes.clear()
        fireStore!!.findNotes(fbId)
    }
//  Find Hillforts
    fun findallHillforts() {
        doAsync {
            val searchedHillforts = fireStore?.findSearchedHillforts()
            val hillfort = fireStore!!.findAllHillforts()
            uiThread {
                if (searchedHillforts != null) {
//                  IF it search is used
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

//  Create Note
    override fun doCreateNote(fireStore: HillfortFireStore?, note: String, hillfort: HillFortModel){
        doAsync {
            fireStore?.createNote(note,hillfort.fbId)
        }
    }

//  Like Hillfort
    override fun doLikeUpdateHillforts(hillfort: HillFortModel){
        doAsync {
            fireStore?.likeHillfort(hillfort)
        }
    }
//  Star Hillfort
    override fun doStarUpdateHillforts(hillfort: HillFortModel){
        doAsync {
            fireStore?.starHillfort(hillfort)
        }
    }
}