package com.example.archaeologicalfieldwork.models

interface HillfortStore {
    fun findAllUsers(): List<UserModel>
    fun createUsers(user: UserModel)
    fun updateUsers(user: UserModel)
    fun getImages(): ArrayList<Images>
    fun findUser(id: Long): UserModel?
    fun findUserByEmail(email: String): UserModel?
    fun findAllHillforts(user: UserModel): List<HillFortModel>
    fun findHillfortsWithStar(user: UserModel): List<HillFortModel?>
    fun findHillfort(marker: String) : HillFortModel?
    fun createHillfort(hillFortModel: HillFortModel, user: UserModel, listofImages: ArrayList<String>)
    fun updateHillforts(hillfort: HillFortModel)
    fun deleteHillforts(hillfort: HillFortModel,user: UserModel)
    fun findCurrentUser():UserModel
    fun deleteUser(user: UserModel)
    fun findNotes(hillfort: HillFortModel): List<Notes>
    fun createNote(notes: Notes)
    fun fetchHills()
    fun findLocation(locationId: Long): Location
    fun findAllImages(): List<Images>
    fun clear()
}