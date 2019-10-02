package com.example.archaeologicalfieldwork.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HillFortModel(
    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var image: String = "",
    var location: Int = 0) : Parcelable
