package com.adithyaegc.todoapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.adithyaegc.todoapp.data.models.ToDo

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_table")
    fun getAllData(): LiveData<List<ToDo>>

    @Insert
    suspend fun insert(toDo: ToDo)

    @Update
    suspend fun update(toDo: ToDo)

    @Delete
    suspend fun deleteItem(toDo: ToDo)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM todo_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery ")
    fun searchDatabase(searchQuery: String): LiveData<List<ToDo>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): LiveData<List<ToDo>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): LiveData<List<ToDo>>
}