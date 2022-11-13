package com.example.dela.domain.model


/**
 * Data class to represent a Task.
 *
 * @property id unique task id
 * @property completed indicates if the task is completed
 * @property title the task title
 * @property description the task description
 * @property categoryId the associated category id
 */
data class Task(
    val id: Long = 0,
    val completed: Boolean = false,
    val title: String,
    val description: String? = null,
    val categoryId: Long? = null
)
