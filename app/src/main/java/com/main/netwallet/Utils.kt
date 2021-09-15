package com.main.netwallet

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService

fun makeStatusNotification(message: String, context: Context, titleNotif: String, ) {

    // Make a channel if necessary
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val name = "Channel Name"
        val descriptionText = "This is sample notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("CHANNEL_ID",name,importance).apply {
            description = descriptionText
        }

        val notificationManager : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    val intent = Intent(context, MainActivity::class.java)
        .putExtra("toNotifFragment", "NotifFragment")
        .apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    val pendingIntent : PendingIntent = PendingIntent.getActivity(context,0, intent, 0)

    var notifBuilder = NotificationCompat.Builder(context, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
        .setContentTitle(titleNotif)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    with(NotificationManagerCompat.from(context)){
        notify(1, notifBuilder)
    }
}