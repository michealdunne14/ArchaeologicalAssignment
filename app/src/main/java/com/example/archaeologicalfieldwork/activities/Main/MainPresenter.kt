package com.example.archaeologicalfieldwork.activities.Main

import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.AddFort.AddFortView
import com.example.archaeologicalfieldwork.adapter.HillFortAdapter
import com.example.archaeologicalfieldwork.adapter.HillFortListener
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.UserModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.jetbrains.anko.intentFor

class MainPresenter(val view: MainView) : HillFortListener {

    var user : UserModel
    var app : MainApp = view.application as MainApp

    init {
        user = app.user
    }

    fun doShowHillforts() {
        var hillforts = app.hillforts.findAllHillforts(user)
        view.mListRecyclerView.adapter = HillFortAdapter(hillforts, this, app, user)
        view.mListRecyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onHillFortClick(hillfort: HillFortModel) {
        view.startActivityForResult(view.intentFor<AddFortView>().putExtra("hillfort_edit", hillfort), 0)
    }

    fun doNavigationDrawer(appBarConfiguration: AppBarConfiguration) {
        val navController = view.findNavController(R.id.host_fragment)
        view.setupActionBarWithNavController(navController,appBarConfiguration)
        val navigation = view.nav_view
        navigation.setupWithNavController(navController)
        val headerView = navigation.getHeaderView(0)
        headerView.mNavName.text = user.name
        headerView.mNavEmail.text = user.email
    }
}