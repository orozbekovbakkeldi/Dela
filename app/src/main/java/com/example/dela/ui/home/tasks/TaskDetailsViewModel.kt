package com.example.dela.ui.home.tasks


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dela.domain.usecase.LoadTask
import com.example.dela.domain.usecase.UpdateTaskCategory
import com.example.dela.domain.usecase.UpdateTaskDescription
import com.example.dela.domain.usecase.UpdateTaskTitle
import com.example.dela.ui.home.CoroutineDebouncer
import com.example.dela.ui.model.task.TaskDetailState
import com.example.dela.ui.model.mapper.TaskMapper
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class TaskDetailsViewModel(
    private val loadTaskUseCase: LoadTask,
    private val taskMapper: TaskMapper,
    private val debounce: CoroutineDebouncer,
    private val updateTaskTitle: UpdateTaskTitle,
    private val updateTaskDescription: UpdateTaskDescription,
    private val updateTaskCategoryUseCase: UpdateTaskCategory
) : ViewModel() {


    fun getTaskInfo(taskId: Long) = flow {
        val task = loadTaskUseCase(taskId)
        if (task == null) {
            emit(TaskDetailState.Error)
        } else {
            emit(TaskDetailState.Loaded(taskMapper.toUI(task)))
        }
    }

    fun updateTitle(taskId: Long, taskTitle: String) {
        debounce(coroutineScope = viewModelScope) {
            updateTaskTitle(taskId, taskTitle)
        }
    }

    fun updateDescription(taskId: Long, taskDescription: String) {
        debounce(coroutineScope = viewModelScope) {
            updateTaskDescription(taskId, taskDescription)
        }
    }

    fun updateTaskCategory(taskId: Long, categoryId: Long?) {
        if (categoryId == null){
            return
        }
        viewModelScope.launch {
            updateTaskCategoryUseCase(taskId, categoryId)
        }
    }
}