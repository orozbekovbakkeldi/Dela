package com.example.dela.ui.model.mapper

import com.example.dela.ui.model.category.Category
import com.example.dela.domain.model.Category as DomainCategory

class CategoryMapper {

    fun toUI(category: DomainCategory) = Category(
        id = category.id,
        name = category.name,
        color = category.color
    )

}