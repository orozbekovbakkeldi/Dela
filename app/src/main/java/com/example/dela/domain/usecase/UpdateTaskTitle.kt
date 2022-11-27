package com.example.dela.domain.usecase

interface UpdateTaskTitle {

    suspend operator fun invoke(taskId: Long, title: String)
}