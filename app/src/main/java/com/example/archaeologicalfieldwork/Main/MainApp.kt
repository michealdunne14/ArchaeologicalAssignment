package com.example.archaeologicalfieldwork.Main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(),AnkoLogger {

    override fun onCreate() {
        super.onCreate()
        info { "App has Started" }
    }
}
