package com.example.archaeologicalfieldwork.activities.Main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.archaeologicalfieldwork.activities.Maps.HillfortMapsView
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.AddFort.AddFortView
import com.example.archaeologicalfieldwork.adapter.HillFortAdapter
import com.example.archaeologicalfieldwork.adapter.HillFortListener
import com.example.archaeologicalfieldwork.models.HillFortModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity

class MainView : AppCompatActivity(),AnkoLogger, HillFortListener {

    lateinit var mainPresenter: MainPresenter

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        info { "Main Activity Started" }
//      Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        mainPresenter = MainPresenter(this)

        toolbar.title = title


        setSupportActionBar(toolbar)

//      Navigation drawer configuration
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mNavHome , R.id.mNavSettings
            ),mMainDrawerLayout
        )

//      Sets up Navigation drawer
        mainPresenter.doNavigationDrawer(appBarConfiguration)
    }

//  Toolbar Add Button
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult(intentFor<AddFortView>(),0)
            R.id.item_map -> startActivity<HillfortMapsView>()
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
        var hillforts = mainPresenter.getHillforts()
        mListRecyclerView.adapter = HillFortAdapter(hillforts, this, mainPresenter.app, mainPresenter.user)
        mListRecyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }

//  Navigate up to host fragment when selected.
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onHillFortClick(hillfort: HillFortModel) {
        startActivityForResult(intentFor<AddFortView>().putExtra("hillfort_edit", hillfort), 0)
    }
}
