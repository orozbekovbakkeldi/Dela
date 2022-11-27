package com.example.dela.ui.model.task

import java.util.*

data class Task(
    val id: Long = 0,
    val completed: Boolean = false,
    val title: String,
    val description: String? = null,
    val categoryId: Long? = null,
    val dueDate: Calendar? = null,
    val alarmInterval: AlarmInterval? = null,
    val repeatable: Boolean = false
)
