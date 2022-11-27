package com.example.dela.domain.impl

import com.example.dela.domain.model.TaskWithCategory
import com.example.dela.domain.repository.SearchRepo
import com.example.dela.domain.usecase.SearchTasksByName
import kotlinx.coroutines.flow.Flow

class SearchTasksByNameImpl(private val searchRepo: SearchRepo) : SearchTasksByName {
    override suspend fun invoke(query: String): Flow<List<TaskWithCategory>> {
        return searchRepo.getTasksByName(query)
    }
}