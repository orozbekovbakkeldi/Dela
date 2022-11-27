package com.example.dela.data.di

import androidx.room.Room
import com.example.dela.data.*
import com.example.dela.data.entity.mapper.CategoryMapper
import com.example.dela.data.entity.mapper.CategoryWithTaskMapper
import com.example.dela.data.entity.mapper.TaskMapper
import com.example.dela.data.roomDb.DelaRoomDb
import com.example.dela.domain.repository.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<TaskWithCategoryRepo> { TasksRepoImpl(get(), get()) }
    single<CategoriesRepo> { CategoriesRepoImpl(get(), get()) }
    single<CategoryRepo> { CategoryRepoImpl(get(), get()) }
    single<TaskRepo> { TaskRepoImpl(get(), get()) }
    single<SearchRepo> { SearchRepoImpl(get(), get()) }
    single {
        Room.databaseBuilder(androidContext(), DelaRoomDb::class.java, "dela_db")
            .fallbackToDestructiveMigration().build()
    }
    single { CategoryMapper() }
    single {
        get<DelaRoomDb>().taskDao()
    }
    single { get<DelaRoomDb>().categoryDao() }
    single { TaskMapper() }
    single { CategoryWithTaskMapper(get(), get()) }

}