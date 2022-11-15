package com.example.dela.domain.impl

import com.example.dela.domain.model.Category
import com.example.dela.domain.repository.CategoryRepo
import com.example.dela.domain.usecase.LoadCategory

class LoadCategoryImpl(private val categoryRepo: CategoryRepo) : LoadCategory {
    override suspend fun invoke(categoryId: Long): Category? {
        return categoryRepo.getCategoryById(categoryId)
    }
}