package com.example.dela.domain.impl

import com.example.dela.domain.model.Task
import com.example.dela.domain.repository.TaskRepo
import com.example.dela.domain.usecase.LoadTask

class LoadTaskImpl(private val taskRepo: TaskRepo): LoadTask {
    override suspend fun invoke(taskId: Long): Task? {
        return taskRepo.findTaskById(taskId)
    }
}