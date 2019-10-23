package com.example.archaeologicalfieldwork.jsonstore

import android.content.Context
import com.example.archaeologicalfieldwork.helper.exists
import com.example.archaeologicalfieldwork.helper.read
import com.example.archaeologicalfieldwork.helper.write
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.HillFortStore
import com.example.archaeologicalfieldwork.models.UserModel
import com.example.archaeologicalfieldwork.models.UserStore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import java.util.*


private val JSON_FILE = "users.json"
private val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
private val listType = object : TypeToken<ArrayList<UserModel>>() {}.type

fun generateRandomIduser(): Long{
    return Random().nextLong()
}

class UsersJsonStore(val context: Context) : UserStore, AnkoLogger {

    var users = mutableListOf<UserModel>()
    var hillforts = mutableListOf<HillFortModel>()

    init {
        if (exists(context, JSON_FILE)){
            deserialize()
        }
    }

    override fun findAll(): List<UserModel> {
        return users
    }

    override fun create(user: UserModel) {
        user.id = generateRandomIduser()
        users.add(user)
        serialize()
    }

    override fun update(user: UserModel) {
        val foundUsers: UserModel? = users.find { hill -> hill.id == user.id }
        if (foundUsers != null){
            foundUsers.name = user.name
            foundUsers.password = user.password
            foundUsers.email = user.email
            foundUsers.id = user.id
            foundUsers.hillforts = user.hillforts
        }
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(users, listType)
        write(context, JSON_FILE,jsonString)
    }

    private fun deserialize(){
        val jsonString = read(context, JSON_FILE)
        users = Gson().fromJson(jsonString, listType)
    }
}