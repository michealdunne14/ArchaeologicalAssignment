package com.example.archaeologicalfieldwork.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.adapter.HillFortAdapter
import com.example.archaeologicalfieldwork.adapter.HillFortListener
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.UserModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult

class MainActivity : AppCompatActivity(),HillFortListener {

    var user = UserModel()
    lateinit var app : MainApp

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        app = application as MainApp

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        toolbar.title = title
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.host_fragment)


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mNavHome , R.id.mNavSettings
            ),mMainDrawerLayout
        )



        setupActionBarWithNavController(navController,appBarConfiguration)
        nav_view.setupWithNavController(navController)

    }

    override fun onStart() {
        val users = app.users.findAll()
        if(users.isNotEmpty()) {
            for (user in users) {
                if (user.email.isEmpty() && user.password.isEmpty()) {
                    startActivity(Intent(this, StartActivity::class.java))
                }
            }
        }else{
            startActivity(Intent(this, StartActivity::class.java))
        }
        super.onStart()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult<AddFortActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.main_menu,menu)
        menuInflater.inflate(R.menu.add_main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        showHillforts(app.hillforts.findAll())
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun showHillforts (hillforts: List<HillFortModel>) {
        mListRecyclerView.adapter = HillFortAdapter(hillforts, this,app)
        mListRecyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onHillFortClick(hillfort: HillFortModel) {
        startActivityForResult(intentFor<AddFortActivity>().putExtra("hillfort_edit", hillfort), 0)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
