package com.example.dela.data

import com.example.dela.data.dao.CategoryDao
import com.example.dela.data.entity.mapper.CategoryMapper
import com.example.dela.domain.CategoriesRepo
import com.example.dela.domain.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoriesRepoImpl(private val categoryDao: CategoryDao, private val mapper: CategoryMapper) :
    CategoriesRepo {
    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getCategories().map {
            mapper.mapToDomain(it)
        }
    }
}