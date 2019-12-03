package com.example.archaeologicalfieldwork.activities.Database

import androidx.room.*
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Notes

@Dao
interface HillfortDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(hillfort: HillFortModel)

    @Query("SELECT * FROM HillFortModel")
    fun findAll(): List<HillFortModel>

    @Query("select * from HillFortModel where id = :id")
    fun findById(id: Long): HillFortModel


    @Update
    fun update(hillfort: HillFortModel)

    @Delete
    fun deleteHillfort(hillfort: HillFortModel)
}
