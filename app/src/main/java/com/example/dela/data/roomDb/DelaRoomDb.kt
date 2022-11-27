package com.example.dela.data.roomDb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dela.data.dao.CategoryDao
import com.example.dela.data.dao.TasksDao
import com.example.dela.data.entity.CategoryEntity
import com.example.dela.data.entity.TaskEntity

@Database(entities = [TaskEntity::class, CategoryEntity::class], version = 3)
abstract class DelaRoomDb : RoomDatabase() {

    abstract fun taskDao(): TasksDao

    abstract fun categoryDao(): CategoryDao
}