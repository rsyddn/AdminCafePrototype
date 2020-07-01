package com.example.admincafeprototype.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Purchased(
    var isClaimed: Boolean? = null,
    var promoId: String? = null,
    var purchasedDate: Date? = null,
    var purchasedId: String? = null,
    var userId: String? = null
) : Parcelable