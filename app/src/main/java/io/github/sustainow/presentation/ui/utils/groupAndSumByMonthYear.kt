package io.github.sustainow.presentation.ui.utils

import io.github.sustainow.domain.model.FormularyAnswer
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun groupAndSumByMonthYear(formularyAnswers: List<FormularyAnswer>): Map<Pair<Int, Int>, Float> {
    return formularyAnswers.groupBy { answer ->
        val dateTime = answer.answerDate.toLocalDateTime(TimeZone.currentSystemDefault())
        Pair(dateTime.year, dateTime.monthNumber)


    }.mapValues { entry ->
        entry.value.map { it.value }.sum() // Alternativa usando `map` + `sum`
    }
}

