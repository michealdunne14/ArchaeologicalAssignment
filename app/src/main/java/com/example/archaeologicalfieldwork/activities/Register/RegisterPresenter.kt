package com.example.archaeologicalfieldwork.activities.Register

import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseActivity.BasePresenter
import com.example.archaeologicalfieldwork.activities.BaseActivity.BaseView
import com.example.archaeologicalfieldwork.activities.BaseActivity.VIEW
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.*

class RegisterPresenter(view: BaseView): BasePresenter(view), AnkoLogger {

    override var app : MainApp = view.application as MainApp
    var user = UserModel()
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun doRegister(email: String, password: String, name: String) {
//        doAsync {
//            val usercheck = app.hillforts.findUserByEmail(email)
//            uiThread {
//                    if (usercheck == null) {
//                        user.name = name
//                        user.email = email
//                        user.password = password
//                        if (user.name.isNotEmpty() && user.email.isNotEmpty() && user.password.isNotEmpty()) {
//                            doCreateUser(user)
//                            view.navigateTo(VIEW.LOGIN)
//                            info { "Main activity has Started" }
//                        }
//                    } else {
//                        view.toast(view.getString(R.string.user_registered_already))
//                    }
//                }
//        }
        view.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view) { task ->
            if (task.isSuccessful) {
                user.name = name
                user.email = email
                user.password = password
                app.hillforts.createUsers(user)
                view.navigateTo(VIEW.LOGIN)
            } else {
                view.toast("Sign Up Failed: ${task.exception?.message}")
            }
            view.hideProgress()
        }
    }


    fun doCreateUser(userModel: UserModel){
        doAsync {
            app.hillforts.createUsers(userModel.copy())
        }
    }

}