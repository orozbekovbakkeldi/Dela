package com.example.dela.domain.impl

import com.example.dela.domain.interactor.AlarmInteractor
import com.example.dela.domain.repository.TaskRepo
import com.example.dela.domain.usecase.ScheduleAlarm
import java.util.*

class ScheduleAlarmImpl(
    private val taskRepo: TaskRepo,
    private val alarmInteractor: AlarmInteractor
) : ScheduleAlarm {
    override suspend fun invoke(taskId: Long, alarm: Calendar) {
        val task = taskRepo.findTaskById(taskId) ?: return
        val updatedTask = task.copy(dueDate = alarm)
        taskRepo.updateTask(updatedTask)
        alarmInteractor.schedule(taskId, alarm.time.time)
    }
}