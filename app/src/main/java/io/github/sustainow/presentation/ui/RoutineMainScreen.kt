package io.github.sustainow.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.sustainow.R
import io.github.sustainow.domain.model.RoutineTask
import io.github.sustainow.presentation.ui.components.TaskCard
import io.github.sustainow.presentation.viewmodel.RoutineViewModel

@Composable
fun RoutineMainScreen(viewModel: RoutineViewModel, navController: NavController) {
    val routine by viewModel.currentRoutine.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    if (isLoading) {
        CircularProgressIndicator()
    } else if (error != null) {
        Text("Erro: $error", color = Color.Red)
    } else {
        if (routine?.taskList.isNullOrEmpty()) {
            NoRoutineMessageWithSuggestions(viewModel, navController)
        } else {
            routine?.let {
                RoutineTaskList(
                    it.taskList,
                    onCheck = { isChecked ->
                        it.taskList[0].id?.let { it1 -> viewModel.updateTaskCompletion(it1) }
                        navController.navigate("createTask")

                    }
                )
            }
        }
    }
}


@Composable
fun RoutineTaskList(tasks: List<RoutineTask>, onCheck: (Boolean) -> Unit) {
    LazyColumn {
        items(tasks) { task ->
            TaskCard(task = task, onCheck = onCheck)
        }
    }
}

@Composable
fun NoRoutineMessageWithSuggestions(viewModel: RoutineViewModel, navController: NavController) {
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
            navController.navigate("createTask")
        }){

            Text(stringResource(R.string.create_task_routine_task_button_add))
        }

//        Text(
//            text = "Sugestões",
//            style = MaterialTheme.typography.headlineSmall,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.padding(bottom = 24.dp)
//        )
//

//
//        val suggestions = viewModel.getMockTasks()
//        SuggestionsList(suggestions, onAddTask = { task ->
//            viewModel.navigateToCreateTask(task, navController) // Redirecionar para a tela de criação
//        })
    }
}
//
//@Composable
//fun SuggestionsList(suggestions: List<RoutineTask>, onAddTask: (RoutineTask) -> Unit) {
//    LazyColumn {
//        items(suggestions) { task ->
//            SuggestionTaskItem(task = task, onAddTask = onAddTask)
//        }
//    }
//}
//
//@Composable
//fun SuggestionTaskItem(task: RoutineTask, onAddTask: (RoutineTask) -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Column {
//            // Faixa colorida no topo
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(8.dp) // Altura da faixa
//                    .background(getTaskColor(task)) // Cor dinâmica
//            )
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Column(modifier = Modifier.weight(1f)) {
//                    Text(
//                        text = task.name,
//                        style = MaterialTheme.typography.headlineSmall
//                    )
//                    Text(
//                        text = task.description ?: "",
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                }
//
//                IconButton(
//                    onClick = { onAddTask(task) },
//                    modifier = Modifier.size(48.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Outlined.Add,
//                        contentDescription = "Adicionar",
//                        tint = Color.Black
//                    )
//                }
//            }
//        }
//    }
//}

// Função para definir a cor da faixa com base no índice ou outro critério
fun getTaskColor(task: RoutineTask): Color {
    return when (task.id?.rem(4)) { // Alternando cores
        0 -> Color(0xFFDDE85C) // Verde claro
        1 -> Color(0xFF7376E6) // Azul
        2 -> Color(0xFFA56E3B) // Marrom
        else -> Color(0xFF58B070) // Verde escuro
    }
}
