package com.example.archaeologicalfieldwork.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.helper.showImagePicker
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import kotlinx.android.synthetic.main.activity_addfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.util.*

class AddFortActivity : AppCompatActivity(),AnkoLogger {

    var hillfort = HillFortModel()
    lateinit var app : MainApp

    var editinghillfort = false
    private val imageList = ArrayList<String>()

    val IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addfort)
        app = application as MainApp

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        info("Add Hill Fort Started..")

        if(intent.hasExtra("hillfort_edit")){
            hillfort = intent.extras?.getParcelable<HillFortModel>("hillfort_edit")!!
            mHillFortName.setText(hillfort.name)
            mHillFortDescription.setText(hillfort.description)

            val viewPager = findViewById<ViewPager>(R.id.mAddFortImagePager)
            val adapter = ImageAdapter(this,hillfort.imageStore)
            viewPager.adapter = adapter
//            mHillFortImage.setImageBitmap(readImageFromPath(this,hillfort.image))
            editinghillfort = true
            mBtnAdd.text = getString(R.string.save_hillfort)
        }

        mBtnAdd.setOnClickListener(){
            hillfort.name = mHillFortName.text.toString()
            hillfort.description = mHillFortDescription.text.toString()
            for (images in imageList) {
                hillfort.imageStore.add(images)
            }
            if (hillfort.name.isNotEmpty()){
                if(editinghillfort){
                    app.hillforts.update(hillfort.copy())
                }else{
                    app.hillforts.create(hillfort.copy())
                }
                info { "add Button Pressed: ${hillfort}" }
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                toast(getString(R.string.addfort_entertitle))
            }
        }

        mAddImage.setOnClickListener{
            showImagePicker(this, IMAGE_REQUEST)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.addfort_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            IMAGE_REQUEST -> {
                if (data != null){
                    hillfort.image = data.data.toString()
//                    mHillFortImage.setImageBitmap(readImage(this,resultCode,data))
                    imageList.add(hillfort.image)
                    val viewPager = findViewById<ViewPager>(R.id.mAddFortImagePager)
                    val adapter = ImageAdapter(this,imageList)
                    viewPager.adapter = adapter
                }
            }
        }
    }
}
