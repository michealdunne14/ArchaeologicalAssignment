package com.example.archaeologicalfieldwork.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.UserModel
import kotlinx.android.synthetic.main.activity_start.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class StartActivity : AppCompatActivity(),AnkoLogger {

    lateinit var app : MainApp
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        info { "Start Activity started" }

//        app = application as MainApp
//        user = app.user

//        if (user.loggedin){
//            startActivity(Intent(this, MainActivity::class.java))
//        }

        mStartLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            info { "Login activity started" }
            finish()
        }

        mStartRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            info { "Register Activity started" }
            finish()
        }
    }
}
