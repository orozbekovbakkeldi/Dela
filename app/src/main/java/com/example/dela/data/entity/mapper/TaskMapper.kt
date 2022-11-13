package com.example.dela.data.entity.mapper

import com.example.dela.data.entity.TaskEntity
import com.example.dela.domain.model.Task

class TaskMapper {

    fun mapTaskToDomain(task: TaskEntity) = Task(
        id = task.id,
        completed = task.completed,
        title = task.title,
        description = task.description,
        categoryId = task.categoryId
    )

    fun mapTaskToEntity(task: Task) = TaskEntity(
        id = task.id,
        title = task.title,
        completed = task.completed,
        description = task.description,
        categoryId = task.categoryId
    )
}