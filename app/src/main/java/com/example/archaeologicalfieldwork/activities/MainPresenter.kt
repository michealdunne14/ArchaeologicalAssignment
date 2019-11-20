package com.example.archaeologicalfieldwork.activities

import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.archaeologicalfieldwork.adapter.HillFortAdapter
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.UserModel
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.intentFor

class MainPresenter(val view: MainActivity) {

    lateinit var user : UserModel
    lateinit var app : MainApp

    init {
        user = app.user
        app = view.application as MainApp
    }

    fun showHillforts (hillforts: List<HillFortModel>) {
//        view.mListRecyclerView.adapter = HillFortAdapter(hillforts, view.baseContext, app, user)
        view.mListRecyclerView.adapter?.notifyDataSetChanged()
    }

}