package com.example.archaeologicalfieldwork.activities

import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.HillFortStore
import com.example.archaeologicalfieldwork.models.UserModel
import com.example.archaeologicalfieldwork.models.UserStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class UserMemoryStore : UserStore, AnkoLogger {
    override fun findUserByEmail(email: String): UserModel? {
        val foundUser: UserModel? = users.find { hill -> hill.email == email}
        return foundUser
    }

    override fun findUser(id: Long): UserModel? {
        val foundUser: UserModel? = users.find { hill -> hill.id == id}
        return foundUser
    }

    val users = ArrayList<UserModel>()

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

    val hillforts = ArrayList<HillFortModel>()

    override fun findAllHillforts(user: UserModel): List<HillFortModel> {
        return hillforts
    }

    override fun createHillfort(hillFortModel: HillFortModel, user: UserModel) {
        hillforts.add(hillFortModel)
        logAll()
    }

    override fun updateHillforts(hillfort: HillFortModel, user: UserModel) {
        val foundHillFort: HillFortModel? = hillforts.find { hill -> hill.id == hillfort.id }
        if (foundHillFort != null){
            foundHillFort.name = hillfort.name
            foundHillFort.description = hillfort.description
            foundHillFort.imageStore = hillfort.imageStore
            foundHillFort.note = hillfort.note
            foundHillFort.location = hillfort.location
            foundHillFort.visitCheck = hillfort.visitCheck
            logAll()
        }    }

    override fun deleteHillforts(hillfort: HillFortModel, user: UserModel) {
        hillforts.remove(hillfort)
        logAll()
    }


    fun logAll() {
        users.forEach{ info("${it}") }
    }
}