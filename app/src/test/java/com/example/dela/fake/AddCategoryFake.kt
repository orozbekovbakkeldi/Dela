package com.example.dela.fake

import com.example.dela.domain.model.Category
import com.example.dela.domain.usecase.AddCategory

class AddCategoryFake : AddCategory {
    private val categories = mutableListOf<Category>()
    override suspend fun invoke(category: Category) {
        categories.add(category)
    }
    fun wasCategoryCreated(name: String) = categories.any() {
        name == it.name
    }
}