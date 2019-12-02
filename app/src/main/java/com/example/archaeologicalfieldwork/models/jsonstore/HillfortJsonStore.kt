package com.example.archaeologicalfieldwork.models.jsonstore

import android.content.Context
import com.example.archaeologicalfieldwork.helper.exists
import com.example.archaeologicalfieldwork.helper.read
import com.example.archaeologicalfieldwork.helper.write
import com.example.archaeologicalfieldwork.models.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import java.util.*
import kotlin.collections.ArrayList


private val JSON_FILE = "hillforts.json"
private val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
private val listType = object : TypeToken<ArrayList<UserModel>>() {}.type

fun generateRandomId(): Long{
    return Random().nextLong()
}

class HillfortJsonStore(val context: Context) : HillfortStore, AnkoLogger {
    override fun findNotes(hillfort: HillFortModel): List<Notes> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findImages(id: Long): List<Images> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createImages(images: Images) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createNote(notes: Notes) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findLocation(locationId: Long): Location {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findCurrentUser(): UserModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAllUsers(): List<UserModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createUsers(user: UserModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateUsers(user: UserModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findUser(id: Long): UserModel? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findUserByEmail(email: String): UserModel? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAllHillforts(user: UserModel): List<HillFortModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findHillfort(user: UserModel, hillfortid: Long): HillFortModel? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createHillfort(hillFortModel: HillFortModel, user: UserModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateHillforts(hillfort: HillFortModel, user: UserModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteHillforts(hillfort: HillFortModel, user: UserModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteUser(user: UserModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var users = mutableListOf<UserModel>()

//    init {
//        if (exists(context, JSON_FILE)){
//            deserialize()
//        }
//    }



//    override fun findAllHillforts(user: UserModel): List<HillFortModel> {
//        return user.hillforts
//    }
//
//    override fun findHillfort(user: UserModel, hillfortid: Long): HillFortModel? {
//        val hillfort: HillFortModel? = user.hillforts.find { hill -> hill.id == hillfortid}
//        return hillfort
//    }
//
//    override fun createHillfort(hillFortModel: HillFortModel, user: UserModel) {
//        hillFortModel.id = generateRandomId()
//        user.hillforts.add(hillFortModel)
//        val foundUsers: UserModel? = users.find { hill -> hill.id == user.id }
//        if (foundUsers != null){
//            foundUsers.hillforts = user.hillforts
//        }
//        serialize()
//    }
//
//    override fun findUser(id: Long): UserModel? {
//        val foundUser: UserModel? = users.find { hill -> hill.id == id}
//        return foundUser
//    }
//
////    override fun findHillfortImages(image: ArrayList<String>) {
////        return image
////    }
//
//    override fun findUserByEmail(email: String): UserModel? {
//        for (user in users){
//            if (user.email == email){
//                return user
//            }
//        }
//        return null
//    }
//
//
//    override fun updateHillforts(hillfort: HillFortModel,user: UserModel) {
//        val foundUser: UserModel? = users.find { hill -> hill.id == user.id }
//
//        if (foundUser != null) {
//            for(hillforts in foundUser.hillforts){
//                if(hillforts.id == hillfort.id) {
//                    hillforts.name = hillfort.name
//                    hillforts.description = hillfort.description
//                    hillforts.imageStore = hillfort.imageStore
//                    hillforts.note = hillfort.note
//                    hillforts.location = hillfort.location
//                    hillforts.visitCheck = hillfort.visitCheck
//                    hillforts.datevisted = hillfort.datevisted
//                    serialize()
//                }
//            }
//        }
//    }
//
//    override fun findHillfortImages(image: String): UserModel {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun deleteHillforts(hillfort: HillFortModel,user: UserModel) {
//        val foundUser: UserModel? = users.find { hill -> hill.id == user.id }
//        if (foundUser != null) {
//            for (hillforts in foundUser.hillforts) {
//                if (hillforts.id == hillfort.id) {
//                    user.hillforts.remove(hillfort)
//                    serialize()
//                    break
//                }
//            }
//        }
//    }
//
//    override fun findAllUsers(): List<UserModel> {
//        return users
//    }
//
//
//    override fun createUsers(user: UserModel) {
//        user.id = generateRandomId()
//        users.add(user)
//        serialize()
//    }
//
//    override fun updateUsers(user: UserModel) {
//        val foundUsers: UserModel? = users.find { hill -> hill.id == user.id }
//        if (foundUsers != null){
//            foundUsers.name = user.name
//            foundUsers.password = user.password
//            foundUsers.email = user.email
//            foundUsers.id = user.id
//            foundUsers.hillforts = user.hillforts
//        }
//        serialize()
//    }
//
//    override fun deleteUser(user: UserModel){
//        val foundUsers: UserModel? = users.find { hill -> hill.id == user.id }
//        if (foundUsers != null){
//            users.remove(foundUsers.copy())
//            serialize()
//        }
//    }
//
//    private fun serialize() {
//        val jsonString = gsonBuilder.toJson(users, listType)
//        write(context, JSON_FILE,jsonString)
//    }
//
//    private fun deserialize(){
//        val jsonString = read(context, JSON_FILE)
//        users = Gson().fromJson(jsonString, listType)
//    }
}