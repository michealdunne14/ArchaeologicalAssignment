package com.example.archaeologicalfieldwork.main

import android.app.Application
import com.example.archaeologicalfieldwork.activities.HillFortMemoryStore
import com.example.archaeologicalfieldwork.activities.UserMemoryStore
import com.example.archaeologicalfieldwork.jsonstore.HillfortJsonStore
import com.example.archaeologicalfieldwork.jsonstore.UsersJsonStore
import com.example.archaeologicalfieldwork.models.HillFortStore
import com.example.archaeologicalfieldwork.models.UserModel
import com.example.archaeologicalfieldwork.models.UserStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

//    lateinit var hillforts: HillFortStore
    lateinit var users: UserStore
    lateinit var user: UserModel

    override fun onCreate() {
        super.onCreate()
//        hillforts = HillfortJsonStore(applicationContext)
        users = UsersJsonStore(applicationContext)
        info { "App has Started" }
    }
}
