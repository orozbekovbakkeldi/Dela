package com.example.dela.domain.provider

import java.util.*

internal class CalendarProviderImpl : CalendarProvider {

    /**
     * Gets the current [Calendar].
     *
     * @return the current [Calendar]
     */
    override fun getCurrentCalendar(): Calendar = Calendar.getInstance()
}
