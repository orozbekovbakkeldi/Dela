package com.example.dela.domain.impl

import com.example.dela.domain.model.AlarmInterval
import com.example.dela.domain.repository.TaskRepo
import com.example.dela.domain.usecase.UpdateTaskAsRepeating

class UpdateTaskAsRepeatingImpl(private val taskRepo: TaskRepo): UpdateTaskAsRepeating {
    override suspend fun invoke(taskId: Long, interval: AlarmInterval?) {
        val task = taskRepo.findTaskById(taskId) ?: return
        val updatedTask = if (interval == null) {
            task.copy(isRepeating = false, alarmInterval = null)
        } else {
            task.copy(isRepeating = true, alarmInterval = interval)
        }
        taskRepo.updateTask(updatedTask)

    }
}