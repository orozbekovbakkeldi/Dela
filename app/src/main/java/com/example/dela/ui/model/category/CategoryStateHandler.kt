package com.example.dela.ui.model.category

data class CategoryStateHandler(
    val state: CategoryUIState = CategoryUIState.Empty,
    val currentCategory: Long? = null,
    val onCategoryChange: (Long?) -> Unit = {}
)
