package com.example.dela.domain.usecase

import com.example.dela.domain.model.Task

interface UpdateTask {

   suspend operator fun invoke(updatedTask: Task)
}