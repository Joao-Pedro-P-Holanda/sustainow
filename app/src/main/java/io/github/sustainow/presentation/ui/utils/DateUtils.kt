package io.github.sustainow.presentation.ui.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
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

fun getLastDayOfCurrentMonth(): LocalDate {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val firstDayOfNextMonth = currentDate.plus(1, DateTimeUnit.MONTH).toJavaLocalDate().withDayOfMonth(1).toKotlinLocalDate()
    return firstDayOfNextMonth.minus(1, DateTimeUnit.DAY)
}

fun getLastDayOfMonth(yearMonth: YearMonth): LocalDate {
    return yearMonth.atEndOfMonth().toKotlinLocalDate()
}

fun getFirstDayOfCurrentMonth(): LocalDate {
    val timeZone = try {
        TimeZone.currentSystemDefault()
    } catch (e: Exception) {
        TimeZone.UTC // Fallback para UTC
    }

    val today = Clock.System.todayIn(timeZone)
    return LocalDate(today.year, today.month, 1)
}


fun getFirstDayOfPreviousMonth(): LocalDate {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val previousMonth = today.minus(DatePeriod(months = 1))
    return LocalDate(previousMonth.year, previousMonth.month, 1)
}

fun getLastDayOfPreviousMonth(): LocalDate {
    val firstDayOfPreviousMonth = getFirstDayOfPreviousMonth()
    val lastDay = firstDayOfPreviousMonth.month.length(firstDayOfPreviousMonth.year)
    return LocalDate(firstDayOfPreviousMonth.year, firstDayOfPreviousMonth.month, lastDay)
}

fun isLeap(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

// Função para obter o número de dias de um mês (considerando anos bissextos)
fun Month.length(year: Int): Int {
    return when (this) {
        Month.FEBRUARY -> if (isLeap(year)) 29 else 28
        Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
        else -> 31
    }
}

fun Instant.toLocalDate(): LocalDate {
    return this.toLocalDateTime(TimeZone.currentSystemDefault()).date
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

