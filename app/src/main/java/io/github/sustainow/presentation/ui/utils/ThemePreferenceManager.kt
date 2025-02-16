package io.github.sustainow.presentation.ui.utils

import android.content.Context
import android.content.SharedPreferences

object ThemePreferenceManager {
    private const val PREF_NAME = "theme_preferences"
    private const val THEME_OPTION_KEY = "theme_option"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getThemeOption(context: Context): ThemeOption {
        val sharedPreferences = getSharedPreferences(context)
        val option = sharedPreferences.getString(THEME_OPTION_KEY, ThemeOption.AUTO.name)
        return ThemeOption.valueOf(option ?: ThemeOption.AUTO.name)
    }

    fun saveThemeOption(context: Context, option: ThemeOption) {
        val sharedPreferences = getSharedPreferences(context)
        sharedPreferences.edit().putString(THEME_OPTION_KEY, option.name).apply()
    }

    fun saveThemePreference(context: Context, isDark: Boolean) {
        val option = if (isDark) ThemeOption.DARK else ThemeOption.LIGHT
        saveThemeOption(context, option)
    }
}

enum class ThemeOption {
    LIGHT, DARK, AUTO
}
