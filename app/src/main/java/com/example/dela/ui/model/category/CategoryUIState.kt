package com.example.dela.ui.model.category

sealed class CategoryUIState{
    data class Loaded(val categories: List<Category>): CategoryUIState()
    object Loading: CategoryUIState()
    object Empty: CategoryUIState()
}