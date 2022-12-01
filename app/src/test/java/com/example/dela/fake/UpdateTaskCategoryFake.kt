package com.example.dela.fake

import com.example.dela.domain.usecase.UpdateTaskCategory

class UpdateTaskCategoryFake : UpdateTaskCategory {

    private val updatedCategories = hashMapOf<Long, Long?>()
    override suspend fun invoke(taskId: Long, categoryId: Long) {
        updatedCategories[taskId] = categoryId
    }

    fun isCategoryUpdated(taskId: Long) = updatedCategories.containsKey(taskId)

    fun getUpdatedTaskCategory(taskId: Long) = updatedCategories[taskId]
}