package io.github.sustainow.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sustainow.R
import io.github.sustainow.exceptions.AuthenticationException
import io.github.sustainow.presentation.ui.utils.InputError
import io.github.sustainow.service.auth.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = LoginViewModel.Factory::class)
class LoginViewModel
    @AssistedInject
    constructor(
        private val authService: AuthService,
        @Assisted("navigateSignUp") private val navigateSignUp: () -> Unit,
        @Assisted("navigateSuccess") private val navigateSuccess: () -> Unit,
    ) : ViewModel() {
        private val _email = MutableStateFlow("")
        val email = _email.asStateFlow()
        private val _emailError = MutableStateFlow<InputError?>(null)
        val emailError = _emailError.asStateFlow()

        private val _password = MutableStateFlow("")
        val password = _password.asStateFlow()
        private val _passwordError = MutableStateFlow<InputError?>(null)
        val passwordError = _passwordError.asStateFlow()

        private val _loadingState = MutableStateFlow(false)
        val loadingState = _loadingState.asStateFlow()

        private val _unknownErrorState = MutableStateFlow(false)
        val unknownErrorState = _unknownErrorState.asStateFlow()

        fun onEmailChange(
            context: Context,
            email: String,
        ) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _emailError.value = InputError(context.getString(R.string.invalid_email_supporting_text))
            } else {
                _emailError.value = null
            }
            _email.value = email
        }

        fun onPasswordChange(password: String) {
            _password.value = password
        }

        fun dismissUnknownErrorDialog() {
            _unknownErrorState.value = false
        }

        fun signInWithEmailAndPassword(
            context: Context,
            email: String,
            password: String,
        ) {
            viewModelScope.launch {
                try {
                    _loadingState.value = true
                    authService.signIn(email, password).collect { success ->
                        if (success) {
                            navigateSuccess()
                        }
                    }
                } catch (e: AuthenticationException) {
                    when (e) {
                        is AuthenticationException.InvalidEmailException -> {
                            _emailError.value = InputError(context.getString(R.string.invalid_email_supporting_text))
                        }
                        is AuthenticationException.InvalidPasswordException -> {
                            _passwordError.value = InputError(context.getString(R.string.invalid_password_supporting_text))
                        }
                        else -> {
                            _unknownErrorState.value = true
                        }
                    }
                } catch (e: Exception) {
                    Log.e("LoginViewModel", e.localizedMessage ?: "Erro desconhecido")
                    _unknownErrorState.value = true
                } finally {
                    if (authService.isUserLoggedIn()) {
                        _loadingState.value = false
                    }
                }
            }
        }

        fun redirectSignUp() {
            navigateSignUp()
        }

        @AssistedFactory
        interface Factory {
            fun create(
                @Assisted("navigateSignUp") navigateSignUp: () -> Unit,
                @Assisted("navigateSuccess") navigateSuccess: () -> Unit,
            ): LoginViewModel
        }
    }
