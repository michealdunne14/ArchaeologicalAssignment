package com.example.archaeologicalfieldwork.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class UserModel(@PrimaryKey(autoGenerate = true)
                     var id: Long = 0,
                     var fbId: String = "",
                     var name: String = "",
                     var password: String = "",
                     var email: String = "") : Parcelable
