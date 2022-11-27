package com.example.dela.di

import com.example.dela.domain.impl.*
import com.example.dela.domain.interactor.AlarmInteractor
import com.example.dela.domain.interactor.NotificationInteractor
import com.example.dela.domain.provider.CalendarProvider
import com.example.dela.domain.provider.CalendarProviderImpl
import com.example.dela.domain.usecase.*
import com.example.dela.domain.usecase.alarm.ScheduleNextAlarm
import com.example.dela.ui.alarm.AlarmInteractorImpl
import com.example.dela.ui.alarm.NotificationInteractorImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val appModule = module {
    factory { CoroutineScope(SupervisorJob()) }
    factory<LoadUnCompletedTasks> { LoadUnCompletedTasksImpl(get()) }
    factory<UpdateTaskStatus> { UpdateTaskStatusImpl(get()) }
    factory<LoadCategories> { LoadCategoriesImpl(get()) }
    factory<AddTask> { AddTaskImpl(get()) }
    factory<AddCategory> { AddCategoryImpl(get()) }
    factory<LoadCategory> { LoadCategoryImpl(get()) }
    factory<UpdateCategory> { UpdateCategoryImpl(get()) }
    factory<DeleteCategory> { DeleteCategoryImpl(get()) }
    factory<UpdateTask> { UpdateTaskImpl(get()) }
    factory<LoadTask> { LoadTaskImpl(get()) }
    factory<UpdateTaskTitle> { UpdateTaskTitleImpl(get(), get()) }
    factory<UpdateTaskDescription> { UpdateTaskDescriptionImpl(get(), get()) }
    factory<UpdateTaskCategory> { UpdateTaskCategoryImpl(get(), get()) }
    factory<UpdateTaskAsRepeating> { UpdateTaskAsRepeatingImpl(get()) }
    factory<SearchTasksByName> { SearchTasksByNameImpl(get()) }
    factory<CancelAlarm> { CancelAlarmImpl(get(), get()) }
    factory<AlarmInteractor> { AlarmInteractorImpl(get()) }
    factory<ScheduleAlarm> { ScheduleAlarmImpl(get(), get()) }
    factory<ShowAlarm> { ShowAlarmImpl(get(), get(), get()) }
    factory<CompleteTask> { CompleteTaskImpl(get(), get(), get()) }
    factory { ScheduleNextAlarm(get(), get(), get()) }
    factory<CalendarProvider> { CalendarProviderImpl() }
    factory<NotificationInteractor> { NotificationInteractorImpl(get(), get()) }
}