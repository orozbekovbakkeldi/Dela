package com.example.dela.ui.model.task

/**
 * Tasks list UI state
 */
sealed class TasksListUIState {
    object Loading : TasksListUIState()
    data class Error(val error: Throwable) : TasksListUIState()
    data class Loaded(val items: List<TaskWithCategory>) : TasksListUIState()
    object Empty : TasksListUIState()
}