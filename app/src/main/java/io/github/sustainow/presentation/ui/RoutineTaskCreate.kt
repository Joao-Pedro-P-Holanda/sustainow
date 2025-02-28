package io.github.sustainow.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.sustainow.domain.model.Task
import io.github.sustainow.presentation.ui.utils.toLocalDate
import io.github.sustainow.presentation.viewmodel.RoutineViewModel
import kotlinx.datetime.Clock

@Composable
fun RoutineTaskCreate(
    navController: NavController,
    viewModel: RoutineViewModel
) {
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = taskName,
            onValueChange = { taskName = it },
            label = { Text("Nome da Tarefa") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = taskDescription,
            onValueChange = { taskDescription = it },
            label = { Text("Descrição da Tarefa") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            val newTask = Task(
                id = viewModel.routine.value?.listOfTasks?.size ?: (0 + 1), // Gera um ID único
                name = taskName,
                description = taskDescription,
                area = "Área Padrão", // Defina a área conforme necessário
                complete = false,
                dueDate = Clock.System.now().toLocalDate(),
                routineId = 1,
                metadataId = 1
            )
            viewModel.addTaskToRoutine(newTask)
            navController.popBackStack() // Volta para a tela anterior após adicionar a tarefa
        }) {
            Text("Adicionar Tarefa")
        }
    }
}
