package com.example.dela.data

import com.example.dela.data.dao.CategoryDao
import com.example.dela.data.entity.mapper.CategoryMapper
import com.example.dela.domain.model.Category
import com.example.dela.domain.repository.CategoryRepo

class CategoryRepoImpl(
    private val categoryDao: CategoryDao,
    private val categoryMapper: CategoryMapper
) : CategoryRepo {

    override suspend fun addCategory(category: Category) {
        categoryDao.addCategory(categoryMapper.mapToEntity(category))
    }

    override suspend fun getCategoryById(categoryId: Long): Category? {
        return categoryDao.getCategory(categoryId)?.let {
            categoryMapper.mapToDomain(it)
        }
    }

    override suspend fun updateCategory(category: Category) {
        return categoryDao.updateCategory(categoryMapper.mapToEntity(category = category))
    }

    override suspend fun deleteCategoryById(id: Long) {
        categoryDao.deleteCategoryById(id)
    }
}