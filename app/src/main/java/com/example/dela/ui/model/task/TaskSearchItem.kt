package com.example.dela.ui.model.task

import androidx.compose.ui.graphics.Color

data class TaskSearchItem(
    val id: Long = 0,
    val completed: Boolean,
    val title: String,
    val categoryColor: Color?,
    val isRepeating: Boolean
)