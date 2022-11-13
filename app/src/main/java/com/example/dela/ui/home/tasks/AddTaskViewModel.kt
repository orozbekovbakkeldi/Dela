package com.example.dela.ui.home.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dela.domain.model.Task
import com.example.dela.domain.usecase.AddTask
import kotlinx.coroutines.launch

class AddTaskViewModel(private val insertTaskUseCase: AddTask) : ViewModel() {

    fun addTask(taskTitle: String, categoryId: Long? = null) {
        if (taskTitle.isEmpty()) {
            return
        }
        viewModelScope.launch {
            insertTaskUseCase(Task(title = taskTitle, categoryId = categoryId))
        }
    }
}