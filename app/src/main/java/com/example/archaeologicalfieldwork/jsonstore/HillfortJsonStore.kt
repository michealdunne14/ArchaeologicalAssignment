package com.example.archaeologicalfieldwork.jsonstore

import android.content.Context
import com.example.archaeologicalfieldwork.helper.exists
import com.example.archaeologicalfieldwork.helper.read
import com.example.archaeologicalfieldwork.helper.write
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.HillFortStore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import java.util.*

private val JSON_FILE = "hillforts.json"
private val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
private val listType = object : TypeToken<ArrayList<HillFortModel>>() {}.type

fun generateRandomId(): Long{
    return Random().nextLong()
}

class HillfortJsonStore(val context: Context) : HillFortStore,AnkoLogger {

    var hillforts = mutableListOf<HillFortModel>()

    init {
        if (exists(context, JSON_FILE)){
            deserialize()
        }
    }

    override fun findAll(): List<HillFortModel> {
        return hillforts
    }

    override fun create(hillfort: HillFortModel) {
        hillfort.id = generateRandomId()
        hillforts.add(hillfort)
        serialize()
    }

    override fun update(hillfort: HillFortModel) {
        val foundHillFort: HillFortModel? = hillforts.find { hill -> hill.id == hillfort.id }
        if (foundHillFort != null){
            foundHillFort.name = hillfort.name
            foundHillFort.description = hillfort.description
            foundHillFort.imageStore = hillfort.imageStore
            foundHillFort.note = hillfort.note
            foundHillFort.location = hillfort.location
            foundHillFort.visitCheck = hillfort.visitCheck
        }
    }

    override fun delete(hillfort: HillFortModel) {
        hillforts.remove(hillfort)
        serialize()
    }

    private fun serialize(){
        val jsonString = gsonBuilder.toJson(hillforts, listType)
        write(context, JSON_FILE,jsonString)
    }

    private fun deserialize(){
        val jsonString = read(context, JSON_FILE)
        hillforts = Gson().fromJson(jsonString, listType)
    }
}