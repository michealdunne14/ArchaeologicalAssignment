package com.example.archaeologicalfieldwork.models

interface HillfortStore {
    fun findAllUsers(): List<UserModel>
    fun createUsers(user: UserModel)
    fun updateUsers(user: UserModel)
    fun findUser(id: Long): UserModel?
    fun findUserByEmail(email: String): UserModel?
    fun findAllHillforts(user: UserModel): List<HillFortModel>
    fun createHillfort(hillFortModel: HillFortModel, user: UserModel)
    fun updateHillforts(hillfort: HillFortModel,user: UserModel)
    fun deleteHillforts(hillfort: HillFortModel,user: UserModel)
    fun deleteUser(user: UserModel)
}