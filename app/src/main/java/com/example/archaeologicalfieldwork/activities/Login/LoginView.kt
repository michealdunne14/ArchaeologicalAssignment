package com.example.archaeologicalfieldwork.activities.Login

import android.os.Bundle
import android.view.View
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseActivity.BaseView
import com.example.archaeologicalfieldwork.activities.BaseActivity.VIEW
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class LoginView : BaseView(), AnkoLogger {
    lateinit var presenter: LoginPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter = initPresenter(LoginPresenter(this)) as LoginPresenter

        progressBar.visibility = View.GONE
//      Login button checking if user is valid
        mLoginButton.setOnClickListener {
            presenter.doLogin(mLoginEmail.text.toString(),mLoginPassword.text.toString())
        }

//      Return to Start page
        mLoginReturn.setOnClickListener {
            navigateTo(VIEW.START)
            info { "Return to Start Activity from Login" }
            finish()
        }
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }
}
