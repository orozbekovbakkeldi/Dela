package com.example.dela.fake

import com.example.dela.domain.usecase.UpdateTaskDescription

class UpdateTaskDescriptionFake : UpdateTaskDescription {

    private val updateDescriptions = hashMapOf<Long, String>()
    override suspend fun invoke(taskId: Long, newDescription: String) {
        updateDescriptions[taskId] = newDescription
    }

    fun isDescriptionUpdated(taskId: Long) = updateDescriptions.containsKey(taskId)

    fun getUpdatedDescription(taskId: Long) = updateDescriptions[taskId]
}