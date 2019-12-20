package com.example.archaeologicalfieldwork.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
@Entity
data class HillFortModel(@PrimaryKey(autoGenerate = true)
                         var id: Long = 0,
                         var name: String = "",
                         var fbId: String = "",
                         var rating: String = "",
                         var description: String = "",
                         var datevisted: String = "",
                         @Embedded
                         var location: Location = Location(),
                         var visitCheck: Boolean = false,
                         var starCheck: Boolean = false) : Parcelable

@Parcelize
@Entity
data class Location(@PrimaryKey(autoGenerate = true)
                    var locationid: Long = 0,
                    var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable

@Parcelize
@Entity
data class Images(@PrimaryKey(autoGenerate = true)
                  var imageid: Long = 0,
                  var hillfortImageid: Long = 0,
                  var hillfortFbid: String = "",
                  var image: String = "") : Parcelable


@Parcelize
@Entity
data class Notes(@PrimaryKey(autoGenerate = true)
                 var noteid: Long = 0,
                 var hillfortNotesid: String = "",
                 var note: String = "") : Parcelable

@Parcelize
@Entity
data class Share(@PrimaryKey(autoGenerate = true)
                 var sharedUser: String = "",
                 var sharedHillfort: String = "") : Parcelable