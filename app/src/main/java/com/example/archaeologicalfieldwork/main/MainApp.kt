package com.example.archaeologicalfieldwork.main

import android.app.Application
import com.example.archaeologicalfieldwork.activities.HillFortMemoryStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

    val hillforts = HillFortMemoryStore()

    override fun onCreate() {
        super.onCreate()
        info { "App has Started" }
    }
}
