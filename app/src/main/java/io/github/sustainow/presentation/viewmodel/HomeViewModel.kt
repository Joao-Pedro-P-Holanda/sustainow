package io.github.sustainow.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sustainow.service.auth.AuthService
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val authService: AuthService,
    ) : ViewModel() {
        fun signOut() {
            viewModelScope.launch {
                authService.signOut()
            }
        }
    }
