package com.example.dela.ui.search

import androidx.lifecycle.ViewModel
import com.example.dela.domain.model.TaskWithCategory
import com.example.dela.domain.usecase.SearchTasksByName
import com.example.dela.ui.model.mapper.TaskSearchMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchViewModel(
    private val findTaskUseCase: SearchTasksByName,
    private val mapper: TaskSearchMapper
) : ViewModel() {

    fun findTasksByName(name: String = ""): Flow<SearchUiState> = flow {
        findTaskUseCase(name).collect { taskList ->
            val state = if (taskList.isNotEmpty()) {
                onListLoaded(taskList)
            } else {
                SearchUiState.Empty
            }
            emit(state)
        }
    }

    private fun onListLoaded(taskList: List<TaskWithCategory>): SearchUiState {
        val searchList = mapper.toTaskSearch(taskList)
        return SearchUiState.Loaded(searchList)
    }
}

