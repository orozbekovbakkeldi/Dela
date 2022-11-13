package com.example.dela.ui.model.mapper

import com.example.dela.ui.model.Task
import com.example.dela.domain.model.Task as DomainTask


class TaskMapper {

    fun toUI(task: DomainTask) =
        Task(
            id = task.id,
            completed = task.completed,
            title = task.title,
            description = task.description,
            categoryId = task.categoryId,
            null,
            null,
            null
        )
}