package io.github.sustainow.domain.model;
import java.time.LocalDate;

class CardConsumeData (
    val realConsume: Float,
    val expectedConsume: Float,
    val unit: String,
    val mes: Int,
    val date: LocalDate,
)