package com.example.dela.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.dela.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("select * from category_table")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Insert
    suspend fun addCategory(category: CategoryEntity)

    @Query("select * from category_table where category_id =:id")
    suspend fun getCategory(id: Long): CategoryEntity?

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Query("delete from category_table where category_id=:id")
    suspend fun deleteCategoryById(id: Long)

    @Query("delete from category_table")
    suspend fun cleanTable()
}