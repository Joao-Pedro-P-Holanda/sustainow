package io.github.sustainow.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.sustainow.domain.model.RoutineTask
import io.github.sustainow.domain.model.RoutineTaskMetaData
import io.github.sustainow.presentation.viewmodel.CreateTaskViewModel
import io.github.sustainow.presentation.viewmodel.RoutineViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


@Composable
fun TaskCreationScreen(
    viewModel: RoutineViewModel,
    navController: NavController
) {
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var taskArea by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = taskName,
            onValueChange = { taskName = it },
            label = { Text("Nome da Tarefa") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = taskDescription,
            onValueChange = { taskDescription = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = taskArea,
            onValueChange = { taskArea = it },
            label = { Text("Área") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { navController.popBackStack() }) {
                Text("Cancelar")
            }
            Button(onClick = {
                val newTask = viewModel.currentRoutine.value?.id?.let {
                    RoutineTaskMetaData(
                        name = taskName,
                        routineId = it,
                        description = taskDescription,
                        area = taskArea,
                        weekdays = emptyList()
                    )
                }
                if (newTask != null) {
                    viewModel.addTaskToRoutine(newTask)
                }
                navController.popBackStack()
            }) {
                Text("Salvar")
            }
        }
    }
}