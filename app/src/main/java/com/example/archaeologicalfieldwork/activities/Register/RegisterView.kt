package com.example.archaeologicalfieldwork.activities.Register

import android.content.Intent
import android.os.Bundle
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseActivity.BaseView
import com.example.archaeologicalfieldwork.activities.Main.MainView
import com.example.archaeologicalfieldwork.activities.BaseActivity.VIEW
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class RegisterView : BaseView(),AnkoLogger {

    lateinit var presenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        presenter = initPresenter(RegisterPresenter(this)) as RegisterPresenter

//      Register Button
        mRegisterButton.setOnClickListener {
            presenter.doRegister(mRegisterEmail.text.toString(),mRegisterPassword.text.toString(),mRegisterName.text.toString())
        }
//      Return Back to Start Screen
        mRegisterReturnButton.setOnClickListener {
            navigateTo(VIEW.START)
            info { "Return to Start Activity from Register" }
            finish()
        }
    }

}
