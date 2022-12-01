package com.example.dela.fake

import com.example.dela.domain.model.Task
import com.example.dela.domain.usecase.AddTask

class AddTaskFake : AddTask {

    private val tasks = mutableListOf<Task>()
    override suspend fun invoke(task: Task) {
        tasks.add(task)
    }

    fun clear() {
        tasks.clear()
    }

    fun wasTaskCreated(title: String) = tasks.any {
        it.title == title
    }
}