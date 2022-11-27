package com.example.dela.domain.usecase

import com.example.dela.domain.model.Task

interface LoadTask {

    suspend operator fun invoke(taskId: Long): Task?
}