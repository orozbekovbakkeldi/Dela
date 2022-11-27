package com.example.dela.domain.impl

import com.example.dela.domain.usecase.LoadTask
import com.example.dela.domain.usecase.UpdateTask
import com.example.dela.domain.usecase.UpdateTaskDescription

class UpdateTaskDescriptionImpl(
    private val loadTask: LoadTask,
    private val updateTask: UpdateTask
) : UpdateTaskDescription {
    override suspend fun invoke(taskId: Long, newDescription: String) {
        val task = loadTask(taskId) ?: return
        val updatedTask = task.copy(description = newDescription)
        updateTask(updatedTask)
    }
}