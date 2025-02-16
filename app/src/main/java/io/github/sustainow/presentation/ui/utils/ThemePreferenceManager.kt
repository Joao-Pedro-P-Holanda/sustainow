package io.github.sustainow.presentation.ui.utils

import android.content.Context
import android.content.SharedPreferences

object ThemePreferenceManager {
    private const val PREF_NAME = "theme_preferences"
    private const val THEME_KEY = "is_dark_theme"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getThemePreference(context: Context): Boolean? {
        val sharedPreferences = getSharedPreferences(context)
        // Recupera a preferência de tema, se não estiver salva, retorna null
        return if (sharedPreferences.contains(THEME_KEY)) {
            sharedPreferences.getBoolean(THEME_KEY, false)
        } else {
            null // Retorna null se a preferência não existir
        }
    }

    fun saveThemePreference(context: Context, isDark: Boolean) {
        val sharedPreferences = getSharedPreferences(context)
        sharedPreferences.edit().putBoolean(THEME_KEY, isDark).apply()
    }
}