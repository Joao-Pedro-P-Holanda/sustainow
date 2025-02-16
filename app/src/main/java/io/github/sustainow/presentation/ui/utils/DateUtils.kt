package io.github.sustainow.presentation.ui.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.YearMonth
import java.util.Calendar
import java.util.Locale

fun getCurrentMonthAbbreviated(): String? {
    val currentCalendar = Calendar.getInstance()
    val monthAsString = currentCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
    return monthAsString
}

fun getCurrentMonthNumber(): Int {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    return  currentDate.monthNumber
}

fun getFirstDayOfCurrentMonth(): LocalDate {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    return LocalDate(currentDate.year, currentDate.monthNumber, 1)
}

fun getLastDayOfCurrentMonth(): LocalDate {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val firstDayOfNextMonth = currentDate.plus(1, DateTimeUnit.MONTH).toJavaLocalDate().withDayOfMonth(1).toKotlinLocalDate()
    return firstDayOfNextMonth.minus(1, DateTimeUnit.DAY)
}

fun getFirstDayOfPreviousMonth(): LocalDate {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    return LocalDate(currentDate.year, currentDate.monthNumber - 1, 1)
}

fun getLastDayOfMonth(yearMonth: YearMonth): LocalDate {
    return yearMonth.atEndOfMonth().toKotlinLocalDate()
}

fun getFirstDayOfCurrentYear(): LocalDate {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    return LocalDate(currentDate.year, 1, 1)
}

fun getCurrentYear(): Int {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    return currentDate.year
}
fun Long.toLocalDate(): LocalDate {
    return Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
}

