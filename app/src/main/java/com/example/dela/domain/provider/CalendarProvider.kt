package com.example.dela.domain.provider

import java.util.*

interface CalendarProvider {

    /**
     * Gets the current [Calendar].
     *
     * @return the current [Calendar]
     */
    fun getCurrentCalendar(): Calendar
}