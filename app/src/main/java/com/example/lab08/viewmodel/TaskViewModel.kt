package com.example.lab08.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab08.data.Task
import com.example.lab08.data.TaskDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskViewModel(private val taskDao: TaskDao) : ViewModel() {

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    fun insert(task: Task) {
        viewModelScope.launch {
            taskDao.insertTask(task)
        }
    }

    fun update(task: Task) {
        viewModelScope.launch {
            taskDao.updateTask(task)
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
        }
    }
}
