package com.example.archaeologicalfieldwork.activities.Login

import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseActivity.BasePresenter
import com.example.archaeologicalfieldwork.activities.BaseActivity.BaseView
import com.example.archaeologicalfieldwork.activities.BaseActivity.VIEW
import com.example.archaeologicalfieldwork.activities.Database.HillfortFireStore
import com.example.archaeologicalfieldwork.main.MainApp
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.*

class LoginPresenter(view: BaseView): BasePresenter(view), AnkoLogger {

    override var app : MainApp = view.application as MainApp
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: HillfortFireStore? = null

    init {
        if (app.hillforts is HillfortFireStore) {
            fireStore = app.hillforts as HillfortFireStore
        }
    }

    fun doLogin(email: String, password: String) {
        view.showProgress()
//        doAsync {
//            val user = app.hillforts.findUserByEmail(email)
//            uiThread {
//                var validName = true
//                if (user?.password == password){
//                    validName = false
//                    view.navigateTo(VIEW.LIST)
//                    info { "Started Main Activity" }
//                    view.finish()
//                }
//
//                if (validName){
//                    view.toast(view.getString(R.string.invalid_login_details))
//                }
//            }
//        }
//      Checks if email and password is not empty
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view) { task ->
//              If User signs in correctly
                if (task.isSuccessful) {
                    if (fireStore != null) {
                        doAsync {
                            fireStore!!.findCurrentUser()
                        }
                        fireStore!!.fetchHillforts {
                            view.hideProgress()
                            view.navigateTo(VIEW.LIST)
                        }
                    } else {
                        view.hideProgress()
                        view.toast("Sign In Failed: ${task.exception?.message}")
                    }
                    view.hideProgress()
                }else{
//                  If user does not exist then send appropriate error
                    view.hideProgress()
                    view.toast("Sign In Failed: ${task.exception?.message}")
                }
            }
        }
    }
}