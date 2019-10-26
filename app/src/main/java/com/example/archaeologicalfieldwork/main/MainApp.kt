package com.example.archaeologicalfieldwork.main

import android.app.Application
import com.example.archaeologicalfieldwork.jsonstore.HillfortJsonStore
import com.example.archaeologicalfieldwork.models.UserModel
import com.example.archaeologicalfieldwork.models.HillfortStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

    lateinit var hillforts: HillfortStore
    lateinit var user: UserModel

    override fun onCreate() {
        super.onCreate()
        hillforts = HillfortJsonStore(applicationContext)
        info { "App has Started" }
    }
}
