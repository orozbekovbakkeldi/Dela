package com.example.dela.data.entity

import androidx.room.Embedded

data class TaskWithCategoryEntity(
    @Embedded val task: TaskEntity,
    @Embedded val category: CategoryEntity? = null
)