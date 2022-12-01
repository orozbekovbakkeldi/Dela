package com.example.dela.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(
    tableName = "task_table", foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["category_id"],
        childColumns = ["task_category_id"],
        onDelete = ForeignKey.CASCADE
    )]
)

data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    val id: Long = 0,
    @ColumnInfo(name = "task_title")
    val title: String,
    @ColumnInfo(name = "task_is_completed")
    val completed: Boolean = false,
    @ColumnInfo(name = "task_description")
    val description: String? = null,
    @ColumnInfo(name = "task_category_id")
    val categoryId: Long? = null,
    val dueDate: Calendar ? = null
)