package com.example.dela.ui.home.category

import androidx.lifecycle.ViewModel
import com.example.dela.domain.usecase.LoadCategories
import com.example.dela.ui.model.category.CategoryUIState
import com.example.dela.ui.model.mapper.CategoryMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryViewModel(
    private val categoryUseCase: LoadCategories,
    private val mapper: CategoryMapper
) : ViewModel() {

    fun getCategories(): Flow<CategoryUIState> {
        return flow {
            categoryUseCase().collect { list ->
                if (list.isEmpty()) {
                    emit(CategoryUIState.Empty)
                } else {
                    emit(CategoryUIState.Loaded(list.map {
                        mapper.toUI(it)
                    }))
                }
            }
        }
    }
}