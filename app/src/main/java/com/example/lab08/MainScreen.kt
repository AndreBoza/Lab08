package com.example.lab08

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab08.data.Task
import com.example.lab08.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(taskViewModel: TaskViewModel) {
    val tasks by taskViewModel.allTasks.collectAsState(initial = emptyList()) // Obtenemos el estado de la lista de tareas
    var newTaskDescription by remember { mutableStateOf("") } // Para capturar la nueva tarea
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Tareas", style = MaterialTheme.typography.titleLarge)

        // Input para agregar una nueva tarea
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            BasicTextField(
                value = newTaskDescription,
                onValueChange = { newTaskDescription = it },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                if (newTaskDescription.isNotEmpty()) {
                    coroutineScope.launch {
                        taskViewModel.insert(Task(description = newTaskDescription))
                        newTaskDescription = "" // Reseteamos el input
                    }
                }
            }) {
                Text("Agregar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar la lista de tareas
        TaskList(tasks = tasks, onTaskCheckedChange = { task, isChecked ->
            coroutineScope.launch {
                taskViewModel.update(task.copy(isCompleted = isChecked))
            }
        })
    }
}

@Composable
fun TaskList(tasks: List<Task>, onTaskCheckedChange: (Task, Boolean) -> Unit) {
    LazyColumn {
        items(tasks.size) { index ->
            val task = tasks[index]
            TaskItem(task = task, onCheckedChange = { isChecked ->
                onTaskCheckedChange(task, isChecked)
            })
        }
    }
}

@Composable
fun TaskItem(task: Task, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(task.description)
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { isChecked -> onCheckedChange(isChecked) }
        )
    }
}
