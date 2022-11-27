package com.example.dela.ui.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat.setExactAndAllowWhileIdle
import com.example.dela.core.getAlarmManager

class TaskNotificationScheduler(private val context: Context) {

    fun scheduleAlarm(taskId: Long, timeInMillis: Long) {
        val intent = Intent(context, TaskReceiver::class.java).apply {
            action = TaskReceiver.ALARM_ACTION
            putExtra(TaskReceiver.EXTRA_TASK, taskId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        context.getAlarmManager()?.let {
            setExactAndAllowWhileIdle(it, AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
        }
    }

    fun cancelAlarm(taskId: Long) {
        val intent = Intent(context, TaskReceiver::class.java)
        intent.action = TaskReceiver.ALARM_ACTION
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        context.getAlarmManager()?.cancel(pendingIntent)
    }
}