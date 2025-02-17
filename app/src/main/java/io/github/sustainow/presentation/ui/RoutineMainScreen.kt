package io.github.sustainow.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.sustainow.domain.model.RoutineTask
import io.github.sustainow.presentation.viewmodel.RoutineViewModel

@Composable
fun RoutineMainScreen(viewModel: RoutineViewModel) {
    val routine by viewModel.currentRoutine.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    if (isLoading) {
        CircularProgressIndicator()
    } else if (error != null) {
        Text("Erro: $error", color = Color.Red)
    } else {
        if (routine == null) {
            // Exibir mensagem inicial e sugestões de tarefas
            NoRoutineMessageWithSuggestions(viewModel)
        } else {
            // Exibir rotina atual
            RoutineTaskList(routine!!.taskList, onTaskComplete = { taskId ->
                viewModel.updateTaskAsComplete(taskId)
            })
        }
    }
}

@Composable
fun RoutineTaskList(tasks: List<RoutineTask>, onTaskComplete: (Int) -> Unit) {
    LazyColumn {
        items(tasks) { task ->
            TaskItem(task = task, onTaskComplete = onTaskComplete)
        }
    }
}
@Composable
fun TaskItem(task: RoutineTask, onAddTask: (RoutineTask) -> Unit = {}, onTaskComplete: (Int) -> Unit = {}) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        Row() {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(task.name, style = MaterialTheme.typography.headlineSmall)
                Text(task.description ?: "", style = MaterialTheme.typography.bodyMedium)

            }
            Button(
                onClick = { onAddTask(task) },
                shape = CircleShape
            ) {
                    Icons.Outlined.Add

            }
        }
    }
}
@Composable
fun NoRoutineMessageWithSuggestions(viewModel: RoutineViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mensagem inicial
        Text(
            text = "Você ainda não tem uma rotina criada.",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Aqui estão algumas sugestões para começar:",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Lista de sugestões de tarefas
        val suggestions = viewModel.getTaskSuggestions()
        SuggestionsList(suggestions, onAddTask = { task ->
            viewModel.addTaskToRoutine(task)
        })
    }
}

@Composable
fun SuggestionsList(suggestions: List<RoutineTask>, onAddTask: (RoutineTask) -> Unit) {
    LazyColumn {
        items(suggestions) { task ->
            SuggestionTaskItem(task = task, onAddTask = onAddTask)
        }
    }
}

@Composable
fun SuggestionTaskItem(task: RoutineTask, onAddTask: (RoutineTask) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = task.description ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

            }
            Button(
                    onClick = { onAddTask(task) },
                    shape = CircleShape,
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task"
                    )
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRoutineMainScreen() {
    RoutineMainScreen(viewModel = RoutineViewModel())
}
