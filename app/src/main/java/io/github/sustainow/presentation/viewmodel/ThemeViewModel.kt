package io.github.sustainow.presentation.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import io.github.sustainow.presentation.ui.utils.ThemeOption
import io.github.sustainow.presentation.ui.utils.ThemePreferenceManager
import io.github.sustainow.presentation.ui.utils.getCurrentTheme

class ThemeViewModel(private val context: Context) : ViewModel() {

    private val _themeOption = mutableStateOf(ThemePreferenceManager.getThemeOption(context))
    val themeOption: State<ThemeOption> = _themeOption

    private val _isDarkTheme = mutableStateOf(isDarkThemeBasedOnOption())
    val isDarkTheme: State<Boolean> = _isDarkTheme

    fun updateTheme(option: ThemeOption) {
        _themeOption.value = option
        ThemePreferenceManager.saveThemeOption(context, option)
        _isDarkTheme.value = isDarkThemeBasedOnOption()
    }

    fun updateThemeFromTime() {
        if (_themeOption.value == ThemeOption.AUTO) {
            _isDarkTheme.value = getCurrentTheme()
        }
    }

    private fun isDarkThemeBasedOnOption(): Boolean {
        return when (_themeOption.value) {
            ThemeOption.LIGHT -> false
            ThemeOption.DARK -> true
            ThemeOption.AUTO -> getCurrentTheme()
        }
    }

    fun updateThemeBasedOnTime() {
        val isNight = getCurrentTheme() // Função que verifica se está de noite
        _isDarkTheme.value = isNight
        ThemePreferenceManager.saveThemePreference(context, isNight)
    }

}
