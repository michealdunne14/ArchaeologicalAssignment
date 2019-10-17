package com.example.archaeologicalfieldwork.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HillFortModel(
    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var image: String = "",
    var imageStore: ArrayList<String> = ArrayList(),
    var location: Location = Location(),
    var note: ArrayList<String> = ArrayList(),
    var visitCheck: Boolean = false) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable
