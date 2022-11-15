package com.example.dela.domain.usecase

import com.example.dela.domain.model.Category

interface LoadCategory {

    suspend operator fun invoke(categoryId: Long): Category?
}