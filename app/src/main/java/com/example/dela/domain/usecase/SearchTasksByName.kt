package com.example.dela.domain.usecase

import com.example.dela.domain.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow

interface SearchTasksByName {
    /**
     * Gets tasks based on the given name.
     *
     * @param query the name to query
     *
     * @return the list of tasks that match the given query
     */
    suspend operator fun invoke(query: String): Flow<List<TaskWithCategory>>
}
