package com.example.dela.ui.di

import com.example.dela.ui.home.category.CategoryViewModel
import com.example.dela.ui.home.tasks.AddTaskViewModel
import com.example.dela.ui.home.tasks.TasksListViewModel
import com.example.dela.ui.model.mapper.CategoryMapper
import com.example.dela.ui.model.mapper.TaskMapper
import com.example.dela.ui.model.mapper.TaskWithCategoryMapper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModules = module {

    factory { TaskMapper() }
    factory { CategoryMapper() }
    factory { TaskWithCategoryMapper(get(), get()) }

    viewModel {
        TasksListViewModel(get(), get(), get())
    }

    viewModel {
        CategoryViewModel(get(), get())
    }

    viewModel {
        AddTaskViewModel(get())
    }
}