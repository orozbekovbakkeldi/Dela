package com.example.dela.domain.repository

import com.example.dela.domain.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow

interface SearchRepo {

    fun getTasksByName(name: String): Flow<List<TaskWithCategory>>
}