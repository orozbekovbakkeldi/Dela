package com.example.dela.domain.usecase

import com.example.dela.domain.model.Task

interface AddTask {

    suspend operator fun invoke(task: Task)
}