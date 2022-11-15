package com.example.dela.ui.home.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dela.domain.usecase.AddCategory
import com.example.dela.ui.model.category.Category
import com.example.dela.ui.model.mapper.CategoryMapper
import kotlinx.coroutines.launch


class CategoryAddViewModel(
    private val addCategoryUseCase: AddCategory,
    private val categoryMapper: CategoryMapper
) : ViewModel() {

    fun addCategory(category: Category) {
        if (category.name.isEmpty()) {
            return
        }

        viewModelScope.launch {
            addCategoryUseCase(categoryMapper.toDomain(category))
        }
    }
}