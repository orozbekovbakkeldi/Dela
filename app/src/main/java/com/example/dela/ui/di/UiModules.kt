package com.example.dela.ui.di

import com.example.dela.domain.usecase.ShowAlarm
import com.example.dela.ui.alarm.*
import com.example.dela.ui.home.CoroutineDebouncer
import com.example.dela.ui.home.DebouncerImpl
import com.example.dela.ui.home.alarm.AlarmViewModel
import com.example.dela.ui.home.category.CategoryAddViewModel
import com.example.dela.ui.home.category.CategoryEditViewModel
import com.example.dela.ui.home.category.CategoryViewModel
import com.example.dela.ui.home.tasks.AddTaskViewModel
import com.example.dela.ui.home.tasks.TaskDetailsViewModel
import com.example.dela.ui.home.tasks.TasksListViewModel
import com.example.dela.ui.model.mapper.*
import com.example.dela.ui.search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModules = module {

    factory { AlarmIntervalMapper() }
    factory { TaskMapper(get()) }
    factory { CategoryMapper() }
    factory { TaskWithCategoryMapper(get(), get()) }
    factory { TaskNotificationScheduler(androidContext()) }
    factory { TaskSearchMapper() }
    viewModel {
        TasksListViewModel(get(), get(), get())
    }

    viewModel {
        CategoryViewModel(get(), get())
    }

    viewModel {
        AddTaskViewModel(get())
    }

    viewModel {
        CategoryAddViewModel(get(), get())
    }

    viewModel {
        CategoryEditViewModel(get(), get(), get(), get())
    }

    viewModel {
        TaskDetailsViewModel(get(), get(), get(), get(), get(), get())
    }

    viewModel {
        AlarmViewModel(get(), get(), get(), get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    factory<CoroutineDebouncer> { DebouncerImpl() }
    factory { TaskNotification(androidContext(), get(), get()) }
    factory { TaskNotificationChannel(androidContext()) }
    factory<AlarmPermission> { AlarmPermissionImpl(get()) }
    factory<PermissionChecker> { PermissionCheckerImpl(get()) }
}