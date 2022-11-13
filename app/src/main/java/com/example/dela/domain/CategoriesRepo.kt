package com.example.dela.domain

import kotlinx.coroutines.flow.Flow
import com.example.dela.domain.model.Category as DomainCategory

interface CategoriesRepo {

    fun getAllCategories(): Flow<List<DomainCategory>>
}