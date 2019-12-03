package com.example.archaeologicalfieldwork.activities.Database

import androidx.room.*
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.UserModel

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(user: UserModel)

    @Query("SELECT * FROM UserModel")
    fun findAll(): List<UserModel>

    @Query("select * from UserModel where id = :id")
    fun findById(id: Long): UserModel

    @Query("select * from UserModel where email = :email")
    fun findByEmail(email: String): UserModel

    @Update
    fun update(user: UserModel)

    @Delete
    fun deleteUser(user: UserModel)
}
