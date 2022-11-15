package com.example.dela.domain.impl

import com.example.dela.domain.model.Category
import com.example.dela.domain.repository.CategoryRepo
import com.example.dela.domain.usecase.UpdateCategory

class UpdateCategoryImpl(private val categoryRepo: CategoryRepo) : UpdateCategory {

    override suspend fun invoke(category: Category) {
        categoryRepo.updateCategory(category)
    }
}