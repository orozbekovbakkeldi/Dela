package com.example.dela.ui.model.mapper

import com.example.dela.ui.model.TaskWithCategory
import com.example.dela.domain.model.TaskWithCategory as DomainTaskWithCategory

class TaskWithCategoryMapper(
    private val taskMapper: TaskMapper,
    private val categoryMapper: CategoryMapper
) {

    fun toUI(list: List<DomainTaskWithCategory>) =
        list.map {
            toUi(it)
        }

    private fun toUi(taskWithCategory: DomainTaskWithCategory) = TaskWithCategory(
        task = taskMapper.toUI(taskWithCategory.task),
        category = taskWithCategory.category?.let {
            categoryMapper.toUI(it)
        }
    )

}