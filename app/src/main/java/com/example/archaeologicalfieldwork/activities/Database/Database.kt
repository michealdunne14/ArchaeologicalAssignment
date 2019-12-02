package com.example.archaeologicalfieldwork.activities.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.archaeologicalfieldwork.models.*

@Database(entities = arrayOf(HillFortModel::class, UserModel::class,Location::class,Images::class,Notes::class), version = 1,  exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun hillfortDao(): HillfortDao
    abstract fun userDao(): UserDao
    abstract fun locationDao(): LocationDao
    abstract fun noteDao(): NotesDao
    abstract fun imageDao(): ImagesDao
}