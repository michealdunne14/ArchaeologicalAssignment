package com.example.archaeologicalfieldwork.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseFragment.BaseFragmentView
import com.example.archaeologicalfieldwork.activities.StartActivity
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_settings.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class SettingsFragView : BaseFragmentView(),AnkoLogger {

    lateinit var settingsFragPresenter: SettingsFragPresenter
    var fbAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        settingsFragPresenter = initPresenter(SettingsFragPresenter(this)) as SettingsFragPresenter

        info { "Settings fragment started" }

        var visitCountUser = 0
//        for (hillforts in user.hillforts){
//            if (hillforts.visitCheck){
//                visitCountUser++
//            }
//        }
//        var count = 0
//        var visitCountTotal = 0
//        for (user in  app.hillforts.findAllUsers()){
//            count += user.hillforts.size
//            for (hillforts in user.hillforts){
//                if (hillforts.visitCheck){
//                    visitCountTotal++
//                }
//            }
//        }
//        view.mSettingsPostsTotal.setText("Total Posts $count")
//        view.mSettingsVisited.setText("Visited per user $visitCountUser")
//        view.mSettingsVisitedTotal.setText("Visited total $visitCountTotal")

//      Logout Button
        view.mLogoutButton.setOnClickListener {
            info { "Logout Button pressed" }
            startActivity(Intent(context, StartActivity::class.java))
            Toast.makeText(context, getString(R.string.LoggedOutuser), Toast.LENGTH_LONG ).show()
            settingsFragPresenter.doLogout()
        }
//      Deleting user
        view.mSettingsDelete.setOnClickListener {
            settingsFragPresenter.doDeleteUser()
        }
//      Updating User
        view.mSettingsUpdate.setOnClickListener {
            info { "User Settings Updated" }
//            user.email = view.mSettingsEmail.text.toString()
//            user.password = view.mSettingsPassword.text.toString()
//            user.name = view.mSettingsName.text.toString()
//            app.hillforts.updateUsers(user.copy())
            Toast.makeText(context, getString(R.string.user_updated), Toast.LENGTH_LONG ).show()
        }


        return view
    }


    override fun onStart() {
        settingsFragPresenter.doShowUser()
        super.onStart()

    }

    override fun doSetDetails(email: String,password: String,name: String){
        //Stats
        view!!.mSettingsEmail.setText(email)
        view!!.mSettingsPassword.setText(password)
        view!!.mSettingsName.setText(name)
//        view.mSettingsPosts.setText("Current User Posts " + user.hillforts.size)
//        view!!.mSettingsUsersTotal.setText("Users " + app.hillforts.findAllUsers().size)
    }
}
