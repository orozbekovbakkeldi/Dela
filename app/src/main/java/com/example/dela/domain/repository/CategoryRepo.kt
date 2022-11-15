package com.example.dela.domain.repository

import com.example.dela.domain.model.Category

interface CategoryRepo {

    suspend fun addCategory(category: Category)

    suspend fun getCategoryById(categoryId: Long): Category?

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategoryById(id: Long)
}