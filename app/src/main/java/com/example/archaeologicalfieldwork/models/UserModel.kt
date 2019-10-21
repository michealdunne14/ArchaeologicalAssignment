package com.example.archaeologicalfieldwork.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

    @Parcelize
    data class UserModel(
        var id: Long = 0,
        var name: String = "",
        var password: String = "",
        var hillforts: HillFortModel = HillFortModel(),
        var email: String = "") : Parcelable
