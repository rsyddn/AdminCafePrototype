package com.example.admincafeprototype.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Claimed(
    var claimedId : String? = null,
    var purcahsedId : String? = null,
    var claimedDate : Date? = null
): Parcelable