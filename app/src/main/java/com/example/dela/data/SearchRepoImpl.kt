package com.example.dela.data

import com.example.dela.data.dao.TasksDao
import com.example.dela.data.entity.mapper.CategoryWithTaskMapper
import com.example.dela.domain.model.TaskWithCategory
import com.example.dela.domain.repository.SearchRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchRepoImpl(
    private val tasksDao: TasksDao,
    private val tasksWithCategoryMapper: CategoryWithTaskMapper
) : SearchRepo {
    override fun getTasksByName(name: String): Flow<List<TaskWithCategory>> {
        val enclosedQuery = "%$name%"
        return tasksDao.findTaskByName(enclosedQuery).map {
            tasksWithCategoryMapper.listToDomain(it)
        }
    }
}