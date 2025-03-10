package io.github.sustainow

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    @androidx.annotation.RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        val notificationChannel= NotificationChannel(
            "daily_tips_channel",
            "Dicas Diárias",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notificações para cadastro de gastos com carbono"
            enableLights(true)
            lightColor = android.graphics.Color.RED
            enableVibration(true)
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}
