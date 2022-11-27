package com.example.dela.domain.impl

import com.example.dela.domain.model.Task
import com.example.dela.domain.repository.TaskRepo
import com.example.dela.domain.usecase.UpdateTask

class UpdateTaskImpl(private val taskRepo: TaskRepo): UpdateTask {

    override suspend fun invoke(updatedTask: Task) {
        taskRepo.updateTask(updatedTask)
    }
}