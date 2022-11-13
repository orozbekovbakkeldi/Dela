package com.example.dela.domain.usecase

import com.example.dela.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface LoadCategories {
    operator fun invoke(): Flow<List<Category>>
}