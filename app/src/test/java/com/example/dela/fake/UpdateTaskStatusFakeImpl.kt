package com.example.dela.fake

import com.example.dela.domain.usecase.UpdateTaskStatus

class UpdateTaskStatusFakeImpl: UpdateTaskStatus {

    private val updatedList = mutableListOf<Long>()

    override suspend fun invoke(taskId: Long) {
        updatedList.add(taskId)
    }

    fun isTaskUpdated(taskId: Long) = updatedList.contains(taskId)
}