package com.example.archaeologicalfieldwork.fragment

import android.content.Intent
import com.example.archaeologicalfieldwork.activities.BaseFragment.BaseFragmentPresenter
import com.example.archaeologicalfieldwork.activities.Database.HillfortFireStore
import com.example.archaeologicalfieldwork.activities.StartActivity
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread

class SettingsFragPresenter(view: SettingsFragView): BaseFragmentPresenter(view),AnkoLogger {

    override var app : MainApp = view.activity?.application as MainApp
    var user = UserModel()
    var fireStore: HillfortFireStore? = null


    init {
        if (app.hillforts is HillfortFireStore) {
            fireStore = app.hillforts as HillfortFireStore
            if (user.email == "") {
                doAsync {
                    user = fireStore!!.currentUser()
                    uiThread {
                        doShowUser()
                    }
                }
            }
        }
    }

//  Show User detail
    private fun doShowUser(){
        view.doSetDetails(user.email, user.password, user.name)
    }

    fun doSettingsUsers(): Long {
        return fireStore!!.totalUsers()
    }

    fun doSettingsHillforts(): Long {
        val users = fireStore!!.totalHillforts()
        return users
    }

    fun doUserHillforts():Int {
        return fireStore!!.userHillforts()
    }

    fun doDeleteUser(){
        view.info { "User Settings Deleted" }
        doAsync {
            fireStore!!.deleteUser(user.copy())
            uiThread {
                doLogout()
            }
        }
    }

    fun doUpdateUsers(userModel: UserModel) {
        userModel.fbId = user.fbId
        fireStore!!.updateUsers(userModel)
    }

    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        fireStore!!.clear()
        view.startActivity(Intent(view.context, StartActivity::class.java))
    }
}