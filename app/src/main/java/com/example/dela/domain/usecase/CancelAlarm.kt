package com.example.dela.domain.usecase

interface CancelAlarm {

    suspend operator fun invoke(taskId: Long)
}