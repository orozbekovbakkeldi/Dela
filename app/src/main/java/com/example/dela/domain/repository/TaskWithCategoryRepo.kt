package com.example.dela.domain.repository

import com.example.dela.domain.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow


interface TaskWithCategoryRepo {

    fun getAllTasksWithCategoryId(categoryId: Long): Flow<List<TaskWithCategory>>

    fun getAllTasksWithCategory(): Flow<List<TaskWithCategory>>

}