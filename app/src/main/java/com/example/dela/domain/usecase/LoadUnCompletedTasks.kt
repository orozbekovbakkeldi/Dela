package com.example.dela.domain.usecase

import com.example.dela.domain.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow

interface LoadUnCompletedTasks {

    operator fun invoke(categoryId: Long? = null): Flow<List<TaskWithCategory>>
}