package com.example.archaeologicalfieldwork.models

interface UserStore {
    fun findAll(): List<UserModel>
    fun create(user: UserModel)
    fun update(user: UserModel)
    fun findUser(id: Long): UserModel?
    fun findUserByEmail(email: String): UserModel?
    fun findAllHillforts(user: UserModel): List<HillFortModel>
    fun createHillfort(hillFortModel: HillFortModel, user: UserModel)
    fun updateHillforts(hillfort: HillFortModel,user: UserModel)
    fun deleteHillforts(hillfort: HillFortModel,user: UserModel)
}