package io.github.sustainow.presentation.ui.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.github.sustainow.presentation.viewmodel.ThemeViewModel

class TimeChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_TIME_TICK || intent?.action == Intent.ACTION_TIME_CHANGED) {
            val themeViewModel = ThemeViewModel(context)
            themeViewModel.updateThemeFromTime()
        }
    }
}
