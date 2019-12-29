package com.example.archaeologicalfieldwork.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseActivity.BaseView
import com.example.archaeologicalfieldwork.activities.BaseActivity.VIEW
import com.example.archaeologicalfieldwork.activities.Login.LoginView
import com.example.archaeologicalfieldwork.activities.Register.RegisterView
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.UserModel
import kotlinx.android.synthetic.main.activity_start.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class StartActivity :  BaseView(),AnkoLogger {

    lateinit var app : MainApp
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        info { "Start Activity started" }
//      Start Login Screen
        mStartLogin.setOnClickListener {
            navigateTo(VIEW.LOGIN)
            info { "Login activity started" }
            finish()
        }
//      Start Register Screen
        mStartRegister.setOnClickListener {
            navigateTo(VIEW.REGISTER)
            info { "Register Activity started" }
            finish()
        }
    }
}
