package com.example.dela.data.entity.mapper

import com.example.dela.data.entity.CategoryEntity
import com.example.dela.domain.model.Category

class CategoryMapper {


    fun mapToDomain(category: CategoryEntity) = Category(
        category.id,
        category.name,
        category.color
    )

    fun mapToDomain(categoryEntities: List<CategoryEntity>) = categoryEntities.map {
        mapToDomain(it)
    }

    fun mapToEntity(category: Category) = CategoryEntity(
        id = category.id,
        name = category.name,
        color = category.color
    )
}