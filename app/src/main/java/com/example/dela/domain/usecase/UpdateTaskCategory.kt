package com.example.dela.domain.usecase

interface UpdateTaskCategory {
    suspend operator fun invoke(taskId: Long, categoryId: Long)
}