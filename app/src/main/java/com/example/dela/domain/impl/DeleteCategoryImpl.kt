package com.example.dela.domain.impl

import com.example.dela.domain.repository.CategoryRepo
import com.example.dela.domain.usecase.DeleteCategory

class DeleteCategoryImpl(private val categoryRepo: CategoryRepo) : DeleteCategory {
    override suspend fun invoke(categoryId: Long) {
        categoryRepo.deleteCategoryById(categoryId)
    }
}