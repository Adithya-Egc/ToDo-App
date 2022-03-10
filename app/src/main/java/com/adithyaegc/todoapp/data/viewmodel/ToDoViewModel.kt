package com.adithyaegc.todoapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.adithyaegc.todoapp.data.ToDoDatabase
import com.adithyaegc.todoapp.data.models.ToDo
import com.adithyaegc.todoapp.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application) {

    private val todoDao = ToDoDatabase.getDatabase(application).todoDao()
    private val todoRepository = TodoRepository(todoDao)

    val getAllData = todoDao.getAllData()

    val sortByHighPriority: LiveData<List<ToDo>> = todoRepository.sortByHighPriority
    val sortByLowPriority: LiveData<List<ToDo>> = todoRepository.sortByLowPriority

    fun insert(toDo: ToDo) = viewModelScope.launch(Dispatchers.IO) { todoRepository.insert(toDo) }

    fun update(toDo: ToDo) = viewModelScope.launch(Dispatchers.IO) { todoRepository.update(toDo) }

    fun deleteItem(toDo: ToDo) =
        viewModelScope.launch(Dispatchers.IO) { todoRepository.deleteItem(toDo) }

    fun deleteAllItems() = viewModelScope.launch(Dispatchers.IO) { todoRepository.deleteAllItems() }

    fun searchDatabase(searchQuery: String): LiveData<List<ToDo>> =
        todoRepository.searchDatabase(searchQuery)

}