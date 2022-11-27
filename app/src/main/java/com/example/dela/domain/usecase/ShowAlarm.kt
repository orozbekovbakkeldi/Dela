package com.example.dela.domain.usecase

interface ShowAlarm {

    suspend operator fun invoke(taskId: Long)
}