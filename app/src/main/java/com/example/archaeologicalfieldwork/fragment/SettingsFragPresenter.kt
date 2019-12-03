package com.example.archaeologicalfieldwork.fragment

import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseActivity.VIEW
import com.example.archaeologicalfieldwork.activities.BaseFragment.BaseFragmentPresenter
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


    init {
        user = app.hillforts.findCurrentUser()
    }


    fun doShowUser(){
        view.doSetDetails(user.email,user.password,user.name)
    }

    fun doDeleteUser(){
        view.info { "User Settings Deleted" }
        doAsync {
        app.hillforts.deleteUser(user.copy())
            uiThread {
                view.startActivity(Intent(view.context, StartActivity::class.java))
                Toast.makeText(view.context, view.getString(R.string.user_deleted), Toast.LENGTH_LONG ).show()
            }
        }
    }

    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        app.hillforts.clear()
        view.startActivity(Intent(view.context, StartActivity::class.java))
    }
}