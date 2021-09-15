package com.main.netwallet.notif

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.main.netwallet.makeStatusNotification

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context != null && intent != null){
            makeStatusNotification("You Have Scheduled Transaction. Click Here To Complete The Transaction", context, "Pending Transaction")
        }
    }
}