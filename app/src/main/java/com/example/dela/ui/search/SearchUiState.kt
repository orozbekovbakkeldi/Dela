package com.example.dela.ui.search

import com.example.dela.ui.model.task.TaskSearchItem

sealed class SearchUiState {

    /**
     * Represents the stated where the screen is loading.
     */
    internal object Loading : SearchUiState()

    /**
     * Represents the stated where the tasks matches the query.
     */
    internal data class Loaded(val taskList: List<TaskSearchItem>) : SearchUiState()

    /**
     * Represents the state where there are no tasks matching the query.
     */
    internal object Empty : SearchUiState()
}