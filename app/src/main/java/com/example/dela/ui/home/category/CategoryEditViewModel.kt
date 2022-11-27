package com.example.dela.ui.home.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dela.domain.usecase.DeleteCategory
import com.example.dela.domain.usecase.LoadCategory
import com.example.dela.domain.usecase.UpdateCategory
import com.example.dela.ui.model.CategorySheetState
import com.example.dela.ui.model.category.Category
import com.example.dela.ui.model.mapper.CategoryMapper
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


class CategoryEditViewModel(
    private val loadCategory: LoadCategory,
    private val updateCategoryUseCase: UpdateCategory,
    private val deleteCategoryUseCase: DeleteCategory,
    private val mapper: CategoryMapper
) : ViewModel() {

    fun getCategory(categoryId: Long) = flow {
        val category = loadCategory.invoke(categoryId)
        if (category == null) {
            emit(CategorySheetState.Empty)
        } else {
            emit(CategorySheetState.Loaded(mapper.toUI(category)))
        }

    }

    fun updateCategory(category: Category) {
        if (category.name.isEmpty()){
            return
        }
        viewModelScope.launch {
            updateCategoryUseCase(mapper.toDomain(category))
        }
    }

    fun deleteCategory(categoryId: Long) {
        viewModelScope.launch {
            deleteCategoryUseCase(categoryId)
        }
    }
}