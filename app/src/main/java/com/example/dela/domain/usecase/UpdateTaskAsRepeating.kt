package com.example.dela.domain.usecase

import com.example.dela.domain.model.AlarmInterval

interface UpdateTaskAsRepeating {
    suspend operator fun invoke(taskId: Long, interval: AlarmInterval?)
}