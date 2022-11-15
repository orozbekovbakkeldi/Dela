package com.example.dela.ui.model

import com.example.dela.ui.model.category.Category

sealed class CategorySheetState {
    object Empty : CategorySheetState()
    data class Loaded(val category: Category) : CategorySheetState()
}