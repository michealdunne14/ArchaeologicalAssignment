package com.example.archaeologicalfieldwork.models

interface HillFortStore {
    fun findAll(): List<HillFortModel>
    fun create(hillfort: HillFortModel)
    fun update(hillfort: HillFortModel)
    fun delete(hillfort: HillFortModel)
}