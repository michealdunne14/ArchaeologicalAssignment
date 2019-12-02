package com.example.archaeologicalfieldwork.activities.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.archaeologicalfieldwork.models.Notes

@Dao
interface NotesDao {

    @Insert
    fun createNote(notes: Notes)

    @Query("select * from Notes where hillfortNotesid = :id")
    fun findNotes(id: Long): List<Notes>

}