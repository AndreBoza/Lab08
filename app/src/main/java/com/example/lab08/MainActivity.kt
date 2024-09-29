package com.example.lab08

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab08.data.TaskDatabase
import com.example.lab08.ui.theme.Lab08Theme
import com.example.lab08.viewmodel.TaskViewModel
import com.example.lab08.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear la instancia de la base de datos
        val database = TaskDatabase.getDatabase(this)
        val taskDao = database.taskDao()

        // Inicializar el ViewModel con el TaskDao
        taskViewModel = ViewModelProvider(this, TaskViewModelFactory(taskDao))[TaskViewModel::class.java]

        setContent {
            Lab08Theme {
                // Llamamos al MainScreen, pasando el ViewModel
                MainScreen(taskViewModel)
            }
        }
    }
}
