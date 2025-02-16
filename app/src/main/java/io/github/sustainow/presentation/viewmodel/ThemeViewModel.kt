package io.github.sustainow.presentation.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import io.github.sustainow.presentation.ui.utils.ThemePreferenceManager
import io.github.sustainow.presentation.ui.utils.getCurrentTheme

class ThemeViewModel(private val context: Context) : ViewModel() {

    private val _isDarkTheme = mutableStateOf(ThemePreferenceManager.getThemePreference(context) ?: getCurrentTheme())
    val isDarkTheme: State<Boolean> = _isDarkTheme

    fun toggleTheme() {
        // Atualizar o estado local
        _isDarkTheme.value = !_isDarkTheme.value
        // Salvar a preferência no armazenamento persistente
        ThemePreferenceManager.saveThemePreference(context, _isDarkTheme.value)
    }
}