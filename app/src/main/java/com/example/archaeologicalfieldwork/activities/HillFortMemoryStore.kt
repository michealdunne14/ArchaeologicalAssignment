package com.example.archaeologicalfieldwork.activities

import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.HillFortStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class HillFortMemoryStore : HillFortStore, AnkoLogger {

    val hillforts = ArrayList<HillFortModel>()


    override fun findAll(): List<HillFortModel> {
        return hillforts
    }

    override fun create(hillfort: HillFortModel) {
        hillforts.add(hillfort)
        logAll()
    }

    override fun update(hillfort: HillFortModel) {
        var foundHillFort: HillFortModel? = hillforts.find { hill -> hill.id == hillfort.id }
        if (foundHillFort != null){
            foundHillFort.name = hillfort.name
            foundHillFort.description = hillfort.description
            foundHillFort.image = hillfort.image
            foundHillFort.location = hillfort.location
            logAll()
        }
    }

    fun logAll() {
        hillforts.forEach{ info("${it}") }
    }

}
