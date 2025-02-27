package io.github.sustainow.presentation.viewmodel

import androidx.lifecycle.ViewModel
import io.github.sustainow.service.auth.AuthService
import kotlinx.coroutines.flow.asStateFlow


class RoutineViewModel(private val authService: AuthService): ViewModel() {

    private val currentUserState = authService.user.asStateFlow()





}