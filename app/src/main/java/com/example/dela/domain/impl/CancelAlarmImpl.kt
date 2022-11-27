package com.example.dela.domain.impl

import com.example.dela.domain.interactor.AlarmInteractor
import com.example.dela.domain.repository.TaskRepo
import com.example.dela.domain.usecase.CancelAlarm

class CancelAlarmImpl(
    private val taskRepo: TaskRepo,
    private val alarmInteractor: AlarmInteractor
) : CancelAlarm {

    override suspend fun invoke(taskId: Long) {
        val task = taskRepo.findTaskById(taskId) ?: return
        val updatedTask = task.copy(dueDate = null)
        taskRepo.updateTask(updatedTask)
        alarmInteractor.cancel(taskId)
    }
}