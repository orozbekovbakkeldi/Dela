package com.example.dela.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.dela.data.entity.TaskEntity
import com.example.dela.data.entity.TaskWithCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Query("select * from task_table left join category_table on task_category_id = category_id")
    fun getAllTasksWithCategory(): Flow<List<TaskWithCategoryEntity>>

    @Query("select * from task_table left join category_table on task_category_id = category_id where task_category_id = :categoryId")
    fun getTasksWithCategoryId(categoryId: Long): Flow<List<TaskWithCategoryEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insertNewTask(task: TaskEntity)

    @Query("select * from task_table where task_id=:id")
    suspend fun findTask(id: Long): TaskEntity?

    @Update(onConflict = REPLACE)
    suspend fun updateTask(task: TaskEntity)

    /**
     * Gets tasks based on the given name.
     *
     * @param query the name to query
     *
     * @return the list of tasks that match the given query
     */
    @Query(
        """SELECT * FROM task_table
            LEFT JOIN category_table ON task_category_id = category_id
            WHERE task_title LIKE :query
            ORDER BY task_is_completed
        """
    )
    fun findTaskByName(query: String): Flow<List<TaskWithCategoryEntity>>

}