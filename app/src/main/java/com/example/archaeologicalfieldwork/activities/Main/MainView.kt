package com.example.archaeologicalfieldwork.activities.Main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseActivity.BaseView
import com.example.archaeologicalfieldwork.adapter.HillFortListener
import com.example.archaeologicalfieldwork.adapter.TabsPagerAdapter
import com.example.archaeologicalfieldwork.animation.ZoomOutPageTransformer
import com.example.archaeologicalfieldwork.fragment.HomeFragView
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Images
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.main_layout.*
import org.jetbrains.anko.*

class MainView : BaseView(),AnkoLogger, HillFortListener,BottomNavigationView.OnNavigationItemSelectedListener {

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
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_main_menu,menu)
        val searchItem: MenuItem? = menu.findItem(R.id.action_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView? = searchItem?.actionView as SearchView
        searchView?.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainPresenter.doSearchForHillforts(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onHillFortClick(
        hillfort: HillFortModel,
        images: ArrayList<Images>
    ) {
        mainPresenter.doEditHillfort(hillfort)
    }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.navigation_main -> {
                val fragment = HomeFragView()
                supportFragmentManager.beginTransaction().replace(R.id.view_pager, fragment, fragment.javaClass.getSimpleName()).commit()
                return true
            }
            R.id.navigation_favourites -> {
                val fragment = HomeFragView()
                supportFragmentManager.beginTransaction().replace(R.id.view_pager, fragment, fragment.javaClass.getSimpleName()).commit()
                return true
            }
        }
        return false
    }
}
