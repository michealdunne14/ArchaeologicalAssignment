package com.example.archaeologicalfieldwork.activities.Database

import android.content.Context
import androidx.room.Room
import com.example.archaeologicalfieldwork.models.*

class StoreRoom(val context: Context){

    var daoHillfort: HillfortDao
    var daoUsers: UserDao
    var daoLocation: LocationDao
    var daoNotes: NotesDao
    var daoImages: ImagesDao
    var user: UserModel = UserModel()

    init {
        val database = Room.databaseBuilder(context,Database::class.java,"room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        daoHillfort = database.hillfortDao()
        daoUsers = database.userDao()
        daoLocation = database.locationDao()
        daoNotes = database.noteDao()
        daoImages = database.imageDao()
    }

//
//    override fun findAllUsers(): List<UserModel> {
//        return daoUsers.findAll()
//    }
//
//    override fun createUsers(user: UserModel) {
//        return daoUsers.create(user)
//    }
//
//    override fun createNote(notes: Notes){
//        val note = daoNotes.createNote(notes)
//        return note
//    }
//
//    override fun fetchHills() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun createImages(images: Images) {
//        return daoImages.createImage(images)
//    }
//
//    override fun updateUsers(user: UserModel) {
//        daoUsers.update(user)
//    }
//
//    override fun findUser(id: Long): UserModel? {
//        user = daoUsers.findById(id)
//        return user
//    }
//
//    override fun findCurrentUser(): UserModel {
//        return user
//    }
//
//    override fun findUserByEmail(email: String):UserModel?{
//        user = daoUsers.findByEmail(email)
//        return user
//    }
//
//    override fun findAllHillforts(user: UserModel): List<HillFortModel> {
//        return daoHillfort.findAll()
//    }
//
//    override fun findHillfortsWithStar(user: UserModel): List<HillFortModel> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun findNotes(hillfort: HillFortModel):List<Notes>{
//        return daoNotes.findNotes(hillfort.id)
//    }
//
//    override fun findImages(fbId: String): List<Images> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//
//    override fun findHillfort(user: UserModel, hillfortid: Long): HillFortModel? {
//        return daoHillfort.findById(hillfortid)
//    }
//
//
//    override fun createHillfort(
//        hillFortModel: HillFortModel,
//        user: UserModel,
//        listofImages: ArrayList<String>
//    ) {
//        return daoHillfort.create(hillFortModel)
//    }
//
////    override fun findImages(fbid: String): List<Images> {
////        return daoImages.findByImage(fbid)
////    }
//
//    override fun findAllImages():List<Images>{
//        return daoImages.findAllImages()
//    }
//
//    override fun clear() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun findLocation(locationId: Long): Location {
//        return daoLocation.findById(locationId)
//    }
//
//    override fun updateHillforts(hillfort: HillFortModel, user: UserModel) {
//        daoHillfort.update(hillfort)
//    }
//
//    override fun deleteHillforts(hillfort: HillFortModel, user: UserModel) {
//        daoHillfort.deleteHillfort(hillfort)
//    }
//
//    override fun deleteUser(user: UserModel) {
//        daoUsers.deleteUser(user)
//    }
}