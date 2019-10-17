package com.example.archaeologicalfieldwork

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.UserModel
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment() {

    lateinit var app : MainApp

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        app = activity?.application as MainApp

        for (user in app.users.findAll()) {
            view.mSettingsEmail.setText(user.email)
            view.mSettingsPassword.setText(user.password)
            view.mSettingsName.setText(user.name)
        }


        return view
    }
}
