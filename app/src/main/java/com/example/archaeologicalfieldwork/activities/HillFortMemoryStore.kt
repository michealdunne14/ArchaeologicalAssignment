package com.example.archaeologicalfieldwork.activities

import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.HillFortStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class HillFortMemoryStore : HillFortStore, AnkoLogger {

//    val hillforts = ArrayList<HillFortModel>()
//
//
//    override fun findAll(): List<HillFortModel> {
//        return hillforts
//    }
//
//
//    override fun create(hillfort: HillFortModel) {
//        hillforts.add(hillfort)
//        logAll()
//    }
//
//    override fun deleteHillforts(hillfort: HillFortModel) {
//        hillforts.remove(hillfort)
//        logAll()
//    }
//
//    override fun updateHillforts(hillfort: HillFortModel) {
//        val foundHillFort: HillFortModel? = hillforts.find { hill -> hill.id == hillfort.id }
//        if (foundHillFort != null){
//            foundHillFort.name = hillfort.name
//            foundHillFort.description = hillfort.description
//            foundHillFort.imageStore = hillfort.imageStore
//            foundHillFort.note = hillfort.note
//            foundHillFort.location = hillfort.location
//            foundHillFort.visitCheck = hillfort.visitCheck
//            logAll()
//        }
//    }
//
//    fun logAll() {
//        hillforts.forEach{ info("${it}") }
//    }

}
