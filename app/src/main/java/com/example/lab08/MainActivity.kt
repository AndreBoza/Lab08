package com.example.lab08

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.lab08.data.TaskDatabase
import com.example.lab08.viewmodel.TaskViewModel
import com.example.lab08.ui.theme.Lab08Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab08Theme {
                // Crear la base de datos y el DAO
                val db = Room.databaseBuilder(
                    applicationContext,
                    TaskDatabase::class.java,
                    "task_db"
                ).build()

                val taskDao = db.taskDao()
                val viewModel = TaskViewModel(taskDao)

                TaskScreen(viewModel)
            }
        }
    }
}

@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    // Observa el estado de la lista de tareas
    val tasks by viewModel.tasks.collectAsState()
    val coroutineScope = rememberCoroutineScope() // Para el uso de corutinas
    var newTaskDescription by remember { mutableStateOf("") } // Estado para la nueva tarea

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Campo de texto para la nueva tarea
        TextField(
            value = newTaskDescription,
            onValueChange = { newTaskDescription = it },
            label = { Text("Nueva tarea") },
            modifier = Modifier.fillMaxWidth()
        )

        // Botón para agregar la tarea
        Button(
            onClick = {
                if (newTaskDescription.isNotEmpty()) {
                    viewModel.addTask(newTaskDescription) // Llama al método para agregar tarea
                    newTaskDescription = "" // Limpia el campo de texto
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Agregar tarea")
        }

        Spacer(modifier = Modifier.height(16.dp)) // Espaciador

        // Lista de tareas
        tasks.forEach { task ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = task.description) // Muestra la descripción de la tarea
                Button(onClick = { viewModel.toggleTaskCompletion(task) }) { // Alterna el estado de completado
                    Text(if (task.isCompleted) "Completada" else "Pendiente")
                }
            }
        }

        // Botón para eliminar todas las tareas
        Button(
            onClick = { coroutineScope.launch { viewModel.deleteAllTasks() } },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Eliminar todas las tareas")
        }
    }
}

// Previsualización para verificar el diseño
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Lab08Theme {
        TaskScreen(TaskViewModel(TaskDatabase::class.java.getDeclaredConstructor().newInstance().taskDao()))
    }
}
