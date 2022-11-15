package com.example.dela.domain.impl

import com.example.dela.domain.repository.TaskRepo
import com.example.dela.domain.usecase.UpdateTaskStatus

class UpdateTaskStatusImpl(private val tasksRepo: TaskRepo) : UpdateTaskStatus {

    override suspend fun invoke(taskId: Long) {
        val task = tasksRepo.findTaskById(taskId) ?: return
        if (task.completed) {
            tasksRepo.unCompleteTask(task)
        } else {
            tasksRepo.completeTask(task)
        }
    }
}