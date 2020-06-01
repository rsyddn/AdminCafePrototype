package com.example.admincafeprototype.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Promo(
    var promoId : String? = null,
    var promoName : String? = null,
    var promoDetail : String? = null,
    var promoStock : Int? = null,
    var promoCost : Int? = null,
    var promoCreateDate : Date? = null,
    var promoExpDate : Date? = null,
    var promoIsActive : Boolean? = null
) : Parcelable