package com.example.dela.ui.model

data class TaskStateHandler(
    val state: TasksListUIState = TasksListUIState.Empty,
    val onCheckedChange: (TaskWithCategory) -> Unit = {},
    val onItemClick: (Long) -> Unit = {},
    val onAddClick: () -> Unit = {}
)
