package io.github.sustainow.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.sustainow.presentation.viewmodel.RoutineViewModel
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter

@Composable
fun RoutineMainScreen(viewModel: RoutineViewModel, modifier: Modifier = Modifier) {
    // Coleta o estado da rotina atual
    val routine by viewModel.currentRoutine.collectAsState()

    // Verifica se a rotina existe e se há tarefas para exibir
    if (routine != null && routine!!.taskList.isNotEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título da rotina
            Text(
                text = "Minha rotina",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(8.dp)
            )

            // Lista de Tarefas
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                routine!!.taskList.forEach { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = task.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = task.date.toJavaLocalDate()
                                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }

            // Botão para adicionar nova tarefa
            Button(
                onClick = { /* Navegar para a tela de adicionar tarefa */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = "Adicionar nova tarefa")
            }
        }
    } else {
        // Caso não haja rotina ou tarefa disponível
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Nenhuma rotina disponível",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
