package io.github.sustainow.presentation.ui.utils

import java.util.Calendar

fun getCurrentTheme(): Boolean {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return currentHour in 18..23 || currentHour in 0..6
}