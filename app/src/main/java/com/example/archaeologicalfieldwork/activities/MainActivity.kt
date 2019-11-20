package com.example.archaeologicalfieldwork.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.archaeologicalfieldwork.HillfortMapsActivity
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.adapter.HillFortAdapter
import com.example.archaeologicalfieldwork.adapter.HillFortListener
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.UserModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(),HillFortListener,AnkoLogger {

    lateinit var user : UserModel
    lateinit var app : MainApp

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        app = application as MainApp

        info { "Main Activity Started" }
//      Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        toolbar.title = title
//      Sets signed user
        user = app.user


        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.host_fragment)


//      Navigation drawer configuration
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mNavHome , R.id.mNavSettings
            ),mMainDrawerLayout
        )

//      Sets up Navigation drawer
        setupActionBarWithNavController(navController,appBarConfiguration)
        nav_view.setupWithNavController(navController)
        val headerView = nav_view.getHeaderView(0)
        headerView.mNavName.text = user.name
        headerView.mNavEmail.text = user.email
    }

//  Toolbar Add Button
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult(intentFor<AddFortActivity>(),0)
            R.id.item_map -> startActivity<HillfortMapsActivity>()
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
        showHillforts(app.hillforts.findAllHillforts(user))
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun showHillforts (hillforts: List<HillFortModel>) {
        mListRecyclerView.adapter = HillFortAdapter(hillforts, this, app, user)
        mListRecyclerView.adapter?.notifyDataSetChanged()
    }
//  Editing a hillfort
    override fun onHillFortClick(hillfort: HillFortModel) {
        startActivityForResult(intentFor<AddFortActivity>().putExtra("hillfort_edit", hillfort), 0)
    }
//  Navigate up to host fragment when selected.
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
