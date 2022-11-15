package com.example.dela.domain.repository

import com.example.dela.domain.model.Task

interface TaskRepo {

    suspend fun insertTask(task: Task)

    suspend fun findTaskById(id: Long): Task?

    suspend fun completeTask(task: Task)

    suspend fun unCompleteTask(task: Task)
}