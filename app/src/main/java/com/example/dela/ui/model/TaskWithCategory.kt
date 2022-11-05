package com.example.dela.ui.model

import com.example.dela.ui.model.category.Category

data class TaskWithCategory(
    val task: Task,
    val category: Category? = null
)
