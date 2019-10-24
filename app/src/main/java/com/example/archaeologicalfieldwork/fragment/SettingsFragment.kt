package com.example.archaeologicalfieldwork.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.UserModel
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment() {

    lateinit var app : MainApp
    var user = UserModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        app = activity?.application as MainApp

        user = app.user
        view.mSettingsEmail.setText(user.email)
        view.mSettingsPassword.setText(user.password)
        view.mSettingsName.setText(user.name)
        view.mSettingsPosts.setText("Current User Posts " + user.hillforts.size)
        view.mSettingsUsersTotal.setText("Users " + app.users.findAll().size)
        var count = 0
        for (user in  app.users.findAll()){
            count += user.hillforts.size
        }
        view.mSettingsPostsTotal.setText("Total Posts $count")

        mSettingsUpdate.setOnClickListener {
            app.users.update(user.copy())
        }


        return view
    }
}
