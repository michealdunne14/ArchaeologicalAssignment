package com.example.archaeologicalfieldwork.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.UserModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

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
            if (user.name.isNotEmpty() && user.email.isNotEmpty() && user.password.isNotEmpty()){
                app.users.create(user.copy())
                app.user = app.users.findUserByEmail(user.email)!!
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        mRegisterReturnButton.setOnClickListener {
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }
    }
}
