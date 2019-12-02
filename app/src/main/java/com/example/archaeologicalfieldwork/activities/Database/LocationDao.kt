package com.example.archaeologicalfieldwork.activities.Database

import androidx.room.*
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Location

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(hillfort: Location)

    @Query("select * from Location where locationid = :id")
    fun findById(id: Long): Location


    @Update
    fun update(hillfort: Location)

    @Delete
    fun deleteHillfort(hillfort: Location)
}