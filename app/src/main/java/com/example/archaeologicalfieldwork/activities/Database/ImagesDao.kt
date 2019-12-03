package com.example.archaeologicalfieldwork.activities.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.archaeologicalfieldwork.models.Images
import com.example.archaeologicalfieldwork.models.Notes

@Dao
interface ImagesDao {

    @Insert
    fun createImage(images: Images)

    @Query("select * from Images where hillfortImageid = :id")
    fun findByImage(id: Long): List<Images>

    @Query("select * from Images")
    fun findAllImages(): List<Images>
}