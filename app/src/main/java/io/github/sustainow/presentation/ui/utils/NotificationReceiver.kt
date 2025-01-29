package io.github.sustainow.presentation.ui.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context)
    }
}

private fun showNotification(context: Context){
    val chanelId = "notification_monthly"
    val chanelName = "Notification"
    val notificationId = 1

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val chanel = NotificationChannel(chanelId, chanelName, NotificationManager.IMPORTANCE_DEFAULT)
    notificationManager.createNotificationChannel(chanel)

    val notification = NotificationCompat.Builder(context, chanelId)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Bom dia!")
        .setContentText("É o primeiro dia do mês. Hora de cadastrar os seus gastos !!!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    notificationManager.notify(notificationId, notification)
}