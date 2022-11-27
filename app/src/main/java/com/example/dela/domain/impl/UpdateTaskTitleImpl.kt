package com.example.dela.domain.impl

import com.example.dela.domain.usecase.LoadTask
import com.example.dela.domain.usecase.UpdateTask
import com.example.dela.domain.usecase.UpdateTaskTitle

class UpdateTaskTitleImpl(
    private val loadTask: LoadTask,
    private val updateTaskUseCase: UpdateTask
) : UpdateTaskTitle {
    override suspend fun invoke(taskId: Long, title: String) {
        val task = loadTask(taskId) ?: return
        val updatedTask = task.copy(title = title)
        updateTaskUseCase(updatedTask)
    }
}

