package com.example.dela.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.dela.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("select * from category_table")
    fun getCategories(): Flow<List<CategoryEntity>>
}