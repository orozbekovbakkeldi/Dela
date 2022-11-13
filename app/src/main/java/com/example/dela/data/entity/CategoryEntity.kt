package com.example.dela.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "category_id")
    val id: Long = 0,
    @ColumnInfo(name = "category_name")
    val name: String,
    @ColumnInfo(name = "category_color")
    val color: Int
)