package com.example.dela.data.entity.mapper

import com.example.dela.data.entity.TaskWithCategoryEntity
import com.example.dela.domain.model.TaskWithCategory

class CategoryWithTaskMapper(
    private val taskMapper: TaskMapper,
    private val categoryMapper: CategoryMapper
) {

    fun listToDomain(taskWithCategoryList: List<TaskWithCategoryEntity>) =
        taskWithCategoryList.map {
            mapToDomain(it)
        }

    private fun mapToDomain(taskWithCategory: TaskWithCategoryEntity) = TaskWithCategory(
        taskMapper.mapTaskToDomain(taskWithCategory.task),
        taskWithCategory.category?.let {
            categoryMapper.mapToDomain(it)
        }
    )
}