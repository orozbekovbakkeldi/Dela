package com.example.dela.domain.usecase

interface CompleteTask {

    suspend operator fun invoke(taskId: Long)
}