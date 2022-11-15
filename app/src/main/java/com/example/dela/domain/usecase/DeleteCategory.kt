package com.example.dela.domain.usecase

interface DeleteCategory {

    suspend operator fun invoke(categoryId: Long)
}