package com.example.dela.ui.model.task

sealed class TaskDetailState {

    object Loading: TaskDetailState()
    object Error: TaskDetailState()
    data class Loaded(val task: Task) : TaskDetailState()

}