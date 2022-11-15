package com.example.dela.domain.impl

import com.example.dela.domain.repository.CategoriesRepo
import com.example.dela.domain.usecase.LoadCategories
import com.example.dela.domain.model.Category
import kotlinx.coroutines.flow.Flow

class LoadCategoriesImpl(private val CategoriesRepo: CategoriesRepo) : LoadCategories {
    override fun invoke(): Flow<List<Category>> {
        return CategoriesRepo.getAllCategories()
    }
}