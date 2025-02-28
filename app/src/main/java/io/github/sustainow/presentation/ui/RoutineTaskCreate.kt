package io.github.sustainow.presentation.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.sustainow.domain.model.Task
import io.github.sustainow.presentation.viewmodel.RoutineViewModel

@Composable
fun RoutineTaskCreate (
    navController: NavController,
    viewModel: RoutineViewModel
){

    fun addTask(task: Task){
        viewModel.addTaskToRoutine(task)
    }
}
