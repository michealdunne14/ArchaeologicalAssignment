package com.example.archaeologicalfieldwork.models

interface HillfortStore {
    fun findAllUsers(): List<UserModel>
    fun createUsers(userModel: UserModel)
    fun updateUsers(user: UserModel)
    fun getImages(): ArrayList<Images>
    fun sharingHillfort(email: String, hillfort: HillFortModel)
    fun findUserByEmail(email: String): UserModel?
    fun findAllHillforts(): List<HillFortModel>
    fun findHillfortsWithStar(): List<HillFortModel?>
    fun findHillfort(marker: String) : HillFortModel?
    fun createHillfort(hillFortModel: HillFortModel, user: UserModel, listofImages: ArrayList<String>)
    fun updateHillforts(hillfort: HillFortModel)
    fun findSearchedHillforts():List<HillFortModel>
    fun clearSearchResult()
    fun deleteHillforts(hillfort: HillFortModel,user: UserModel)
    fun findCurrentUser()
    fun getSharedImages(): ArrayList<Images>
    fun currentUser():UserModel
    fun getSharedHillforts(): List<HillFortModel>
    fun deleteUser(user: UserModel)
    fun fetchHills()
    fun findLocation(locationId: Long): Location
    fun findAllImages(): List<Images>
    fun createNote(note: String, fbId: String)
    fun clear()
}