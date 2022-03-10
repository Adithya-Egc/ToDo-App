package com.adithyaegc.todoapp.data.repository

import androidx.lifecycle.LiveData
import com.adithyaegc.todoapp.data.TodoDao
import com.adithyaegc.todoapp.data.models.ToDo

class TodoRepository(private val dao: TodoDao) {


    val getAllData: LiveData<List<ToDo>> = dao.getAllData()
    val sortByHighPriority: LiveData<List<ToDo>> = dao.sortByHighPriority()
    val sortByLowPriority: LiveData<List<ToDo>> = dao.sortByLowPriority()

    suspend fun insert(toDo: ToDo) = dao.insert(toDo)

    suspend fun update(toDo: ToDo) = dao.update(toDo)

    suspend fun deleteItem(toDo: ToDo) = dao.deleteItem(toDo)

    suspend fun deleteAllItems() = dao.deleteAll()

    fun searchDatabase(searchQuery: String) = dao.searchDatabase(searchQuery)


}