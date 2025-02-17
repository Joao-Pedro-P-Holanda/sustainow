package io.github.sustainow.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.sustainow.presentation.viewmodel.CreateTaskViewModel

@Composable
fun CreateTaskScreen(viewModel: CreateTaskViewModel) {
    val taskMetaData by viewModel.taskMetaData.collectAsState()

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            Text("Nome da Tarefa", style = MaterialTheme.typography.headlineSmall)
            BasicTextField(
                value = taskMetaData.name,
                onValueChange = { viewModel.updateName(it) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Text("Descrição", style = MaterialTheme.typography.headlineSmall)
            BasicTextField(
                value = taskMetaData.description ?: "",
                onValueChange = { viewModel.updateDescription(it) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Text("Dias da semana", style = MaterialTheme.typography.headlineSmall)
            WeekdaySelector(
                selectedWeekdays = taskMetaData.weekdays,
                onWeekdaySelected = { viewModel.updateWeekdays(it) }
            )
        }
        item {
            Text("Áreas de impacto", style = MaterialTheme.typography.headlineSmall)
            AreaSelector(
                selectedArea = taskMetaData.area,
                onAreaSelected = { viewModel.updateArea(it) }
            )
        }
        item {
            Button(onClick = { viewModel.saveTask() }) {
                Text("Salvar")
            }
        }
    }
}

@Composable
fun WeekdaySelector(selectedWeekdays: List<Int>, onWeekdaySelected: (List<Int>) -> Unit) {
    // Implementação do seletor de dias da semana
}

@Composable
fun AreaSelector(selectedArea: String, onAreaSelected: (String) -> Unit) {
    // Implementação do seletor de áreas de impacto
}