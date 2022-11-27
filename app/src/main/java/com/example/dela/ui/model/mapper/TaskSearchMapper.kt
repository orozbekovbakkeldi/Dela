package com.example.dela.ui.model.mapper

import androidx.compose.ui.graphics.Color
import com.example.dela.domain.model.TaskWithCategory
import com.example.dela.ui.model.task.TaskSearchItem

class TaskSearchMapper {

    fun toTaskSearch(taskList: List<TaskWithCategory>): List<TaskSearchItem> =
        taskList.map(::toTaskSearch)

    private fun toTaskSearch(taskWithCategory: TaskWithCategory): TaskSearchItem =
        TaskSearchItem(
            id = taskWithCategory.task.id,
            completed = taskWithCategory.task.completed,
            title = taskWithCategory.task.title,
            categoryColor = getColor(taskWithCategory.category?.color),
            isRepeating = taskWithCategory.task.isRepeating
        )

    private fun getColor(color: String?): Color? {
        if (color == null) {
            return null
        }

        return Color(android.graphics.Color.parseColor(color))
    }
}