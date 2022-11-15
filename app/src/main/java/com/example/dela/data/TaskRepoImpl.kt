package com.example.dela.data

import com.example.dela.data.dao.TasksDao
import com.example.dela.data.entity.mapper.TaskMapper
import com.example.dela.domain.repository.TaskRepo
import com.example.dela.domain.model.Task

class TaskRepoImpl(private val tasksDao: TasksDao, val mapper: TaskMapper) : TaskRepo {
    override suspend fun insertTask(task: Task) {
        tasksDao.insertNewTask(mapper.mapTaskToEntity(task))
    }

    override suspend fun findTaskById(id: Long): Task? {
        return tasksDao.findTask(id)?.let { task ->
            mapper.mapTaskToDomain(task)
        }
    }

    override suspend fun completeTask(task: Task) {
        val updatedTask = mapper.mapTaskToEntity(task).copy(completed = true)
        tasksDao.updateTask(updatedTask)
    }

    override suspend fun unCompleteTask(task: Task) {
        tasksDao.updateTask(mapper.mapTaskToEntity(task).copy(completed = false))
    }
}