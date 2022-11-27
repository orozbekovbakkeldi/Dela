package com.example.dela.domain.usecase

interface UpdateTaskDescription {

    suspend operator fun invoke(taskId: Long, newDescription: String)
}