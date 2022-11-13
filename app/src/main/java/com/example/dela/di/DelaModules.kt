package com.example.dela.di

import com.example.dela.domain.impl.AddTaskImpl
import com.example.dela.domain.impl.LoadCategoriesImpl
import com.example.dela.domain.impl.LoadUnCompletedTasksImpl
import com.example.dela.domain.impl.UpdateTaskStatusImpl
import com.example.dela.domain.usecase.AddTask
import com.example.dela.domain.usecase.LoadCategories
import com.example.dela.domain.usecase.LoadUnCompletedTasks
import com.example.dela.domain.usecase.UpdateTaskStatus
import org.koin.dsl.module

val appModule = module {
    factory<LoadUnCompletedTasks> { LoadUnCompletedTasksImpl(get()) }
    factory<UpdateTaskStatus> { UpdateTaskStatusImpl(get()) }
    factory<LoadCategories> { LoadCategoriesImpl(get()) }
    factory<AddTask> { AddTaskImpl(get()) }
}