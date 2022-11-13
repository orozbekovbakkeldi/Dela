package com.example.dela.ui.home.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dela.domain.usecase.LoadUnCompletedTasks
import com.example.dela.domain.usecase.UpdateTaskStatus
import com.example.dela.ui.model.TaskWithCategory
import com.example.dela.ui.model.TasksListUIState
import com.example.dela.ui.model.mapper.TaskWithCategoryMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TasksListViewModel(
    val tasksUseCase: LoadUnCompletedTasks,
    val updateTaskStatusUseCase: UpdateTaskStatus,
    val mapper: TaskWithCategoryMapper
) : ViewModel() {

    fun getTasksList(categoryId: Long? = null): Flow<TasksListUIState> {

        return flow {
            val tasks = tasksUseCase(categoryId)
            tasks.map {
                mapper.toUI(it)
            }.catch { error ->
                emit(TasksListUIState.Error(error))
            }.collect {
                if (it.isEmpty()){
                    emit(TasksListUIState.Empty)
                } else {
                    emit(TasksListUIState.Loaded(it))
                }
            }
        }
    }

    fun updateTaskStatus(taskWithCategory: TaskWithCategory) {
        viewModelScope.launch {
            updateTaskStatusUseCase(taskWithCategory.task.id)
        }
    }

}