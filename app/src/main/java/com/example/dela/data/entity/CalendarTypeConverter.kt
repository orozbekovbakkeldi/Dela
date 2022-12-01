package com.example.dela.data.entity

import androidx.room.TypeConverter
import java.util.Calendar

class CalendarTypeConverter {

    @TypeConverter
    fun fromCalendarToLong(calendar: Calendar?) = calendar?.timeInMillis

    @TypeConverter
    fun toCalendar(time: Long?) =
        time?.let { t ->
            Calendar.getInstance().apply {
                timeInMillis = t
            }
        }
}