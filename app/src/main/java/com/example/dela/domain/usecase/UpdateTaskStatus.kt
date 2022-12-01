package com.example.dela.domain.usecase

interface UpdateTaskStatus {

    suspend operator fun invoke(taskId: Long)
}