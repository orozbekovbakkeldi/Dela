package com.example.dela.ui.model.mapper

import com.example.dela.ui.model.task.Task
import com.example.dela.domain.model.Task as DomainTask


class TaskMapper(private val alarmIntervalMapper: AlarmIntervalMapper) {

    fun toUI(task: DomainTask) =
        Task(
            id = task.id,
            completed = task.completed,
            title = task.title,
            description = task.description,
            categoryId = task.categoryId,
            dueDate = task.dueDate,
            alarmInterval = alarmIntervalMapper.mapToUI(task.alarmInterval),
            repeatable = task.isRepeating
        )
}