package com.example.dela.domain.impl

import com.example.dela.domain.model.Category
import com.example.dela.domain.repository.CategoryRepo
import com.example.dela.domain.usecase.AddCategory

class AddCategoryImpl(private val categoryRepo: CategoryRepo) : AddCategory {
    override suspend fun invoke(category: Category) {
        categoryRepo.addCategory(category)
    }
}