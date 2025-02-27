package io.github.sustainow.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.sustainow.domain.model.Task
import io.github.sustainow.presentation.ui.components.TaskCard
import io.github.sustainow.presentation.viewmodel.RoutineViewModel

@Composable
fun RoutineMainScreen(viewModel: RoutineViewModel, navController: NavController) {
    // Observa os estados do ViewModel
    val isLoading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val routine by viewModel.routine.collectAsState()
    val suggestedTasks by viewModel.suggestedTasks.collectAsState()

    // Chama fetchSuggestedTasks quando a tela é carregada
    LaunchedEffect(Unit) {
        viewModel.fetchSuggestedTasks()
    }

    // Exibe o estado de carregamento
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    // Exibe o estado de erro
    else if (error != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Erro: $error", color = Color.Red)
        }
    }
    // Exibe a lista de tarefas ou a mensagem de "nenhuma rotina"
    else {
        if (routine?.listOfTasks.isNullOrEmpty()) {
            NoRoutineMessageWithSuggestions(viewModel, navController, suggestedTasks)
        } else {
            routine?.let { currentRoutine ->
                RoutineTaskList(
                    tasks = currentRoutine.listOfTasks,
                    onCheck = { taskId ->
                        viewModel.completeTask(taskId)
                    }
                )
            }
        }
    }
}
@Composable
fun RoutineTaskList(tasks: List<Task>, onCheck: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(tasks) { task ->
            TaskCard(
                task = task,
                onCheck = { isChecked ->
                    if (isChecked) {
                        onCheck(task.id)
                    }
                }
            )
        }
    }
}

@Composable
fun NoRoutineMessageWithSuggestions(
    viewModel: RoutineViewModel,
    navController: NavController,
    suggestedTasks: List<Task>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nenhuma rotina adicionada",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Comece a monitorar seus hábitos de consumo e veja a evolução ao longo do tempo",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(onClick = {
            navController.navigate("Routines")
        }) {
            Text("Adicionar Tarefa")
        }

        // Exibe sugestões de tarefas, se houver
        if (suggestedTasks.isNotEmpty()) {
            Text(
                text = "Sugestões",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
            )
            SuggestionsList(suggestedTasks, onAddTask = { task ->
                viewModel.addTaskToRoutine(task)
            })
        }
    }
}
@Composable
fun SuggestionsList(suggestions: List<Task>, onAddTask: (Task) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(suggestions) { task ->
            SuggestionTaskItem(task = task, onAddTask = onAddTask)
        }
    }
}

@Composable
fun SuggestionTaskItem(task: Task, onAddTask: (Task) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Faixa colorida no topo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(getTaskColor(task))
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.name,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                IconButton(
                    onClick = { onAddTask(task) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "Adicionar",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

// Função para definir a cor da faixa com base no ID da tarefa
fun getTaskColor(task: Task): Color {
    return when (task.id.rem(4)) { // Alternando cores
        0 -> Color(0xFFDDE85C) // Verde claro
        1 -> Color(0xFF7376E6) // Azul
        2 -> Color(0xFFA56E3B) // Marrom
        else -> Color(0xFF58B070) // Verde escuro
    }
}