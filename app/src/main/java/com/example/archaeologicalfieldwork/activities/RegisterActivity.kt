package com.example.archaeologicalfieldwork.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.UserModel
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class RegisterActivity : AppCompatActivity(),AnkoLogger {

    lateinit var app : MainApp
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        app = application as MainApp


        mRegisterButton.setOnClickListener {
            user.name = mRegisterName.text.toString()
            user.email = mRegisterEmail.text.toString()
            user.password = mRegisterPassword.text.toString()
            val usercheck = app.hillforts.findUserByEmail(user.email)
            if (usercheck == null) {
                if (user.name.isNotEmpty() && user.email.isNotEmpty() && user.password.isNotEmpty()) {
                    app.hillforts.createUsers(user.copy())
                    startActivity(Intent(this, LoginActivity::class.java))
                    info { "Main activity has Started" }
                    finish()
                }
            }else{
                toast("User already registered")
            }
        }

        mRegisterReturnButton.setOnClickListener {
            startActivity(Intent(this, StartActivity::class.java))
            info { "Return to Start Activity from Register" }
            finish()
        }
    }
}
