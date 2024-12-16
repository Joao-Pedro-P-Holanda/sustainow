package io.github.sustainow.presentation.ui.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.Calendar
import java.util.Locale

fun getCurrentDate(): LocalDate  {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
}

fun getCurrentMonthAbbreviated(): String? {
    val currentCalendar = Calendar.getInstance()
    val monthAsString = currentCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
    return monthAsString
}

fun getCurrentMonthNumber(): Int {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    return currentDate.monthNumber
}

fun getCurrentYear(): Int {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    return currentDate.year
}
