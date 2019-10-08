package com.example.archaeologicalfieldwork.activities

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.readImageFromPath
import kotlinx.android.synthetic.main.activity_addfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class AddFortActivity : AppCompatActivity(),AnkoLogger {

    var hillfort = HillFortModel()
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addfort)
        app = application as MainApp

        info("Add Hill Fort Started..")

        btnAdd.setOnClickListener(){
            hillfort.name = hillFortName.text.toString()
            hillfort.description = hillFortDescription.text.toString()
            hillFortImage.setImageBitmap(readImageFromPath(this,hillfort.image))
            if (hillfort.name.isNotEmpty()){
                app.hillforts.create(hillfort.copy())
                info { "add Button Pressed: ${hillfort}" }
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                toast(getString(R.string.addfort_entertitle))
            }
        }
    }
}
