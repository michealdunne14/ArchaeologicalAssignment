package com.example.archaeologicalfieldwork.activities.Main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.PagerAdapter
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseActivity.BaseView
import com.example.archaeologicalfieldwork.adapter.HillFortAdapter
import com.example.archaeologicalfieldwork.adapter.HillFortListener
import com.example.archaeologicalfieldwork.adapter.TabsPagerAdapter
import com.example.archaeologicalfieldwork.animation.ZoomOutPageTransformer
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.main_layout.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread

class MainView : BaseView(),AnkoLogger, HillFortListener {

    lateinit var mainPresenter: MainPresenter
    val user = FirebaseAuth.getInstance().currentUser
    lateinit var pagerAdapter: TabsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        info { "Main Activity Started" }

                // Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = title
        setSupportActionBar(toolbar)

        mainPresenter = initPresenter(MainPresenter(this)) as MainPresenter

        pagerAdapter = TabsPagerAdapter(supportFragmentManager)
        view_pager.setPageTransformer(true, ZoomOutPageTransformer())
        view_pager.adapter = pagerAdapter
        view_pager.currentItem = 1

    }

//  Toolbar Add Button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> mainPresenter.doAddHillfort()
            R.id.item_map -> mainPresenter.doShowHillfortMap()
        }
        return super.onOptionsItemSelected(item)
    }
//  adds menu to toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
//  Shows hillforts when result
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    doAsync {
        val hillforts = mainPresenter.getHillforts()
        uiThread {
            activityResult(hillforts)
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
    override fun onHillFortClick(hillfort: HillFortModel) {
        mainPresenter.doEditHillfort(hillfort)
    }

    fun activityResult(hillforts: List<HillFortModel>) {
//        mListRecyclerView.adapter = HillFortAdapter(hillforts, this, mainPresenter.app, mainPresenter.user)
//        mListRecyclerView.adapter?.notifyDataSetChanged()
    }
}
