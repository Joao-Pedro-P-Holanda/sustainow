package io.github.sustainow.presentation.ui.utils

import io.github.sustainow.domain.model.FormularyAnswer
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun groupAndSumByMonthYearWithStartEnd(formularyAnswers: List<FormularyAnswer>): Map<Pair<Int, Int>, Pair<Float, Float>> {
    return formularyAnswers.groupBy { answer ->
        val dateTime = answer.answerDate.toLocalDateTime(TimeZone.currentSystemDefault())
        Pair(dateTime.year, dateTime.monthNumber)
    }.mapValues { entry ->
        val answers = entry.value
        val expectedSum = answers.filter {
            it.type == "expected"
        }.map { it.value }.sum()

        val realSum = answers.filter {
            it.type == "real"
        }.map { it.value }.sum()

        Pair(expectedSum, realSum)
    }
}

