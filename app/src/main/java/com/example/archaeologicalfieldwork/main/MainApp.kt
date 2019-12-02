package com.example.archaeologicalfieldwork.main

import android.app.Application
import com.example.archaeologicalfieldwork.activities.Database.StoreRoom
import com.example.archaeologicalfieldwork.models.jsonstore.HillfortJsonStore
import com.example.archaeologicalfieldwork.models.UserModel
import com.example.archaeologicalfieldwork.models.HillfortStore
import com.example.archaeologicalfieldwork.models.Location
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

    lateinit var hillforts: HillfortStore

    override fun onCreate() {
        super.onCreate()
        hillforts = StoreRoom(applicationContext)
        info { "App has Started" }
    }
}
