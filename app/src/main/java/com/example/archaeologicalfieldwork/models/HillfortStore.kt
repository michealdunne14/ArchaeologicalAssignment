package com.example.archaeologicalfieldwork.models

interface HillfortStore {
    fun findAllUsers(): List<UserModel>
    fun createUsers(user: UserModel)
    fun updateUsers(user: UserModel)
    fun findUser(id: Long): UserModel?
    fun findUserByEmail(email: String): UserModel?
    fun findAllHillforts(user: UserModel): List<HillFortModel>
    fun findHillfort(user: UserModel, hillfortid: Long) : HillFortModel?
    fun createHillfort(hillFortModel: HillFortModel, user: UserModel)
    fun updateHillforts(hillfort: HillFortModel,user: UserModel)
    fun deleteHillforts(hillfort: HillFortModel,user: UserModel)
    fun findCurrentUser():UserModel
    fun deleteUser(user: UserModel)
    fun findNotes(hillfort: HillFortModel): List<Notes>
    fun findImages(id: Long): List<Images>
    fun createImages(images: Images)
    fun createNote(notes: Notes)
    fun findLocation(locationId: Long): Location
    fun findAllImages(): List<Images>
    fun clear()
}