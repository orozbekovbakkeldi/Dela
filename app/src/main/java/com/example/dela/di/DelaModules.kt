package com.example.dela.di

import com.example.dela.domain.impl.*
import com.example.dela.domain.usecase.*
import org.koin.dsl.module

val appModule = module {
    factory<LoadUnCompletedTasks> { LoadUnCompletedTasksImpl(get()) }
    factory<UpdateTaskStatus> { UpdateTaskStatusImpl(get()) }
    factory<LoadCategories> { LoadCategoriesImpl(get()) }
    factory<AddTask> { AddTaskImpl(get()) }
    factory<AddCategory> { AddCategoryImpl(get()) }
    factory<LoadCategory> { LoadCategoryImpl(get()) }
    factory<UpdateCategory> { UpdateCategoryImpl(get()) }
    factory<DeleteCategory> { DeleteCategoryImpl(get()) }
}