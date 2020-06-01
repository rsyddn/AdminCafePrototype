package com.example.admincafeprototype.util

import java.util.*

object CalendarHelper {

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun getCurrentDateInMills() : Long{
        return Calendar.getInstance().timeInMillis
    }

    fun getCurrentDateMax(maxSize : Long) : Long{

        return Calendar.getInstance().timeInMillis + maxSize
    }
}