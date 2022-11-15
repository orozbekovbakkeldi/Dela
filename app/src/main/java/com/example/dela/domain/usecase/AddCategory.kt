package com.example.dela.domain.usecase

import com.example.dela.domain.model.Category

interface AddCategory {

    suspend operator fun invoke(category: Category)
}