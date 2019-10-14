package com.example.archaeologicalfieldwork.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.main.MainApp
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        app = application as MainApp

        mLoginButton.setOnClickListener {
            val users = app.users.findAll()

            for (i in users){
                if (i.email == mLoginEmail.text.toString() && i.password == mLoginPassword.text.toString()){
                    startActivity(Intent(this, MainActivity::class.java))
                    app.users.findOne(i)
                    finish()
                }
            }

        }

        mLoginReturn.setOnClickListener {
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }
    }
}
