package com.example.dela.domain.impl

import com.example.dela.domain.TaskWithCategoryRepo
import com.example.dela.domain.usecase.LoadUnCompletedTasks
import com.example.dela.domain.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoadUnCompletedTasksImpl(private val tasksRepo: TaskWithCategoryRepo) : LoadUnCompletedTasks {
    override fun invoke(categoryId: Long?): Flow<List<TaskWithCategory>> {
        return categoryId?.let { id ->
            tasksRepo.getAllTasksWithCategoryId(id).map {
                it.filterNot { taskWithCategory ->
                    taskWithCategory.task.completed
                }
            }
        } ?: tasksRepo.getAllTasksWithCategory().map {
            it.filterNot { taskWithCategory ->
                taskWithCategory.task.completed
            }
        }

    }
}