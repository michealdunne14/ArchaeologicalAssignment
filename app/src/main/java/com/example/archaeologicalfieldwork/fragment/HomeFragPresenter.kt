package com.example.archaeologicalfieldwork.fragment

import com.example.archaeologicalfieldwork.activities.BaseFragment.BaseFragmentPresenter
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.Notes
import com.example.archaeologicalfieldwork.models.UserModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class HomeFragPresenter(view: HomeFragView): BaseFragmentPresenter(view) {
    var user = UserModel()
    override var app : MainApp = view.activity?.application as MainApp

    init {
        user = app.hillforts.findCurrentUser()
    }

    fun findallHillforts() {
        doAsync {
            val hillfort = app.hillforts.findAllHillforts(user)
            uiThread {
                view.showHillforts(hillfort,user)
            }
        }
    }

    fun createNotes(note: Notes) {
        app.hillforts.createNote(note)
    }
}