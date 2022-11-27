package com.example.dela.ui.model.task

import com.example.dela.ui.model.category.Category
import com.example.dela.ui.model.task.Task

data class TaskWithCategory(
    val task: Task,
    val category: Category?
)
