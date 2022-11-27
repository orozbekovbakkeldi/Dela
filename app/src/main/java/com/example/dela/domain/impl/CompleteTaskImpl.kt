package com.example.dela.domain.impl

import com.example.dela.domain.interactor.AlarmInteractor
import com.example.dela.domain.interactor.NotificationInteractor
import com.example.dela.domain.repository.TaskRepo
import com.example.dela.domain.usecase.CompleteTask

class CompleteTaskImpl(
    private val taskRepo: TaskRepo,
    private val alarmInteractor: AlarmInteractor,
    private val notificationInteractor: NotificationInteractor
) : CompleteTask {

    override suspend fun invoke(taskId: Long) {
        val task = taskRepo.findTaskById(taskId) ?: return
        val updateTask = task.copy(completed = true)
        taskRepo.updateTask(updateTask)
        alarmInteractor.cancel(taskId)
        notificationInteractor.dismiss(taskId)

    }
}