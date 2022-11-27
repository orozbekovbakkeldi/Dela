package com.example.dela.domain.impl

import com.example.dela.domain.usecase.LoadTask
import com.example.dela.domain.usecase.UpdateTask
import com.example.dela.domain.usecase.UpdateTaskCategory

class UpdateTaskCategoryImpl(private val loadTask: LoadTask, private val updateTask: UpdateTask) :
    UpdateTaskCategory {
    override suspend fun invoke(taskId: Long, categoryId: Long) {
        val task = loadTask(taskId) ?: return
        updateTask(task.copy(categoryId = categoryId))
    }
}