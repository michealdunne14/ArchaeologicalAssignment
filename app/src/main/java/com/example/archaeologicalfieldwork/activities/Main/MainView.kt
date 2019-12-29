package com.example.archaeologicalfieldwork.activities.Main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseActivity.BaseView
import com.example.archaeologicalfieldwork.adapter.HillFortListener
import com.example.archaeologicalfieldwork.adapter.TabsPagerAdapter
import com.example.archaeologicalfieldwork.animation.ZoomOutPageTransformer
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Images
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.main_layout.*
import org.jetbrains.anko.*

class MainView : BaseView(),AnkoLogger, HillFortListener {

    lateinit var mainPresenter: MainPresenter
    val user = FirebaseAuth.getInstance().currentUser
    lateinit var pagerAdapter: TabsPagerAdapter
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        info { "Main Activity Started" }

        // Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = title
        setSupportActionBar(toolbar)

        mainPresenter = initPresenter(MainPresenter(this)) as MainPresenter
//        val navController = findNavController(R.id.view_pager)
//        val navView: BottomNavigationView = findViewById(R.id.navigationView)
//
//
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.mNavHome , R.id.mNavSettings, R.id.mNavFavourites
//            ),mMainDrawerLayout
//        )

//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)

//
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.action_recents:
//                    Toast.makeText(MainActivity.this, "Recents", Toast.LENGTH_SHORT).show();
//                    break;
//                    case R.id.action_favorites:
//                    Toast.makeText(MainActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
//                    break;
//                    case R.id.action_nearby:
//                    Toast.makeText(MainActivity.this, "Nearby", Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                return true;
//            }
//        });
//    }
//     Set up of bottom naviagation bar
        navigationView.selectedItemId = R.id.mNavHome
        pagerAdapter = TabsPagerAdapter(supportFragmentManager)
        view_pager.setPageTransformer(true, ZoomOutPageTransformer())
        view_pager.adapter = pagerAdapter
        view_pager.currentItem = 1
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        navigationView.selectedItemId = R.id.mNavSettings
                    }
                    1 -> {

                        navigationView.selectedItemId = R.id.mNavHome
                    }
                    2 -> {
                        navigationView.selectedItemId = R.id.mNavFavourites
                    }
                    else -> navigationView.selectedItemId = R.id.mNavHome
                }
            }
        })
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
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
//  Navigating to the correct selected Item
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.mNavSettings -> {
                view_pager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.mNavHome -> {
                view_pager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.mNavFavourites -> {
                view_pager.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onHillFortClick(
        hillfort: HillFortModel,
        images: ArrayList<Images>
    ) {
        mainPresenter.doEditHillfort(hillfort)
    }

}
