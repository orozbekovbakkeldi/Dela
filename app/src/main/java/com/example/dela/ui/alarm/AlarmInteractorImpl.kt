package com.example.dela.ui.alarm

import com.example.dela.domain.interactor.AlarmInteractor

class AlarmInteractorImpl(private val alarmManager: TaskNotificationScheduler) : AlarmInteractor {

    override fun schedule(alarmId: Long, timeInMillis: Long) {
        alarmManager.scheduleAlarm(alarmId, timeInMillis)
    }

    override fun cancel(alarmId: Long) {
        alarmManager.cancelAlarm(alarmId)
    }
}