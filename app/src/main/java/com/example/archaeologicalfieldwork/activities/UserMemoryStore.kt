package com.example.archaeologicalfieldwork.activities

import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.UserModel
import com.example.archaeologicalfieldwork.models.UserStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class UserMemoryStore : UserStore, AnkoLogger {

    val users = ArrayList<UserModel>()
    val hillforts = ArrayList<HillFortModel>()

    override fun create(user: UserModel) {
        users.add(user)
        logAll()
    }

    override fun update(user: UserModel) {
//        var foundUser: UserModel? = users.find { currentuser -> currentuser.id == users.nam}

    }

    override fun findAll(): List<UserModel> {
        return users
    }


    fun logAll() {
        users.forEach{ info("${it}") }
    }
}