package com.example.dela.data

import com.example.dela.data.entity.mapper.CategoryWithTaskMapper
import com.example.dela.data.roomDb.DelaRoomDb
import com.example.dela.domain.repository.TaskWithCategoryRepo
import kotlinx.coroutines.flow.map

class TasksRepoImpl(
    private val roomDb: DelaRoomDb,
    private val mapper: CategoryWithTaskMapper
) :
    TaskWithCategoryRepo {

    override fun getAllTasksWithCategoryId(categoryId: Long) =
        roomDb.taskDao().getTasksWithCategoryId(categoryId).map {
            mapper.listToDomain(it)
        }


    override fun getAllTasksWithCategory() =
        roomDb.taskDao().getAllTasksWithCategory().map {
            mapper.listToDomain(it)
        }

}