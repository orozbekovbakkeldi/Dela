package com.example.dela.domain.impl

import com.example.dela.domain.interactor.NotificationInteractor
import com.example.dela.domain.repository.TaskRepo
import com.example.dela.domain.usecase.ShowAlarm
import com.example.dela.domain.usecase.alarm.ScheduleNextAlarm

class ShowAlarmImpl(
    private val taskRepo: TaskRepo,
    private val notificationInteractor: NotificationInteractor,
    private val scheduleNextAlarm: ScheduleNextAlarm
) : ShowAlarm {

    override suspend fun invoke(taskId: Long) {
        val task = taskRepo.findTaskById(taskId) ?: return

        if (task.completed) {
            return
        } else {
            notificationInteractor.show(task)
        }

        if (task.isRepeating) {
            scheduleNextAlarm(task)
        }
    }
}