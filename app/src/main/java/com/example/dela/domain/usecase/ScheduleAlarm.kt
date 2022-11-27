package com.example.dela.domain.usecase

import java.util.*

interface ScheduleAlarm {

    suspend operator fun invoke(taskId: Long, alarm: Calendar)
}