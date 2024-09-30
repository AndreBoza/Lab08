package com.example.lab08.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab08.data.Task
import com.example.lab08.data.TaskDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel(private val taskDao: TaskDao) : ViewModel() {

    // MutableStateFlow para almacenar las tareas
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks // Exponer el StateFlow

    // Propiedad para exponer las tareas como Flow
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    init {
        // Cargar las tareas desde el DAO cuando se inicializa el ViewModel
        viewModelScope.launch {
            taskDao.getAllTasks().collect { taskList ->
                _tasks.value = taskList // Actualizar el StateFlow
            }
        }
    }

    // Función para insertar una nueva tarea
    fun insert(task: Task) {
        viewModelScope.launch {
            taskDao.insertTask(task) // Llama al método del DAO para insertar la tarea
        }
    }

    // Función para agregar una nueva tarea a partir de la descripción
    fun addTask(description: String) {
        val newTask = Task(description = description) // Crear una nueva instancia de Task
        insert(newTask) // Llama a la función insert
    }

    // Función para actualizar una tarea existente
    fun update(task: Task) {
        viewModelScope.launch {
            taskDao.updateTask(task)
        }
    }

    // Función para eliminar una tarea específica
    fun delete(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
        }
    }

    // Implementación de la función para eliminar todas las tareas
    fun deleteAllTasks() {
        viewModelScope.launch {
            taskDao.deleteAllTasks() // Llama al método del DAO para eliminar todas las tareas
        }
    }

    // Función para alternar el estado de completado de la tarea
    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            taskDao.updateTask(updatedTask) // Actualiza la tarea en la base de datos
        }
    }
}
