package com.example.dela.domain.impl

import com.example.dela.domain.TaskRepo
import com.example.dela.domain.model.Task
import com.example.dela.domain.usecase.AddTask

class AddTaskImpl(private val taskRepo: TaskRepo) : AddTask {
    override suspend fun invoke(task: Task) {
        taskRepo.insertTask(task)
    }
}