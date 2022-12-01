package com.example.dela.fake

import com.example.dela.domain.usecase.UpdateTaskTitle

class UpdateTaskTitleFake: UpdateTaskTitle {

    private val updatedMap = hashMapOf<Long, String>()
    override suspend fun invoke(taskId: Long, title: String) {
        updatedMap[taskId] = title
    }

    fun isTaskTitleUpdated(taskId: Long) = updatedMap.containsKey(taskId)

    fun getUpdateTaskTitle(taskId: Long) = updatedMap[taskId]
}