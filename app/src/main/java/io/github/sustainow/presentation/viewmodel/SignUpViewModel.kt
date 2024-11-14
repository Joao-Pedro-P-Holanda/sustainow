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

@HiltViewModel(assistedFactory = SignUpViewModel.Factory::class)
class SignUpViewModel
    @AssistedInject
    constructor(
        private val authService: AuthService,
        @Assisted("navigateLogin") private val navigateLogin: () -> Unit,
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

        private val _confirmPassword = MutableStateFlow("")
        val confirmPassword = _confirmPassword.asStateFlow()
        private val _confirmPasswordError = MutableStateFlow<InputError?>(null)
        val confirmPasswordError = _confirmPasswordError.asStateFlow()

        private val _firstName = MutableStateFlow("")
        val firstName = _firstName.asStateFlow()
        private val _firstNameError = MutableStateFlow<InputError?>(null)
        val firstNameError = _firstNameError.asStateFlow()

        private val _lastName = MutableStateFlow("")
        val lastName = _lastName.asStateFlow()
        private val _lastNameError = MutableStateFlow<InputError?>(null)
        val lastNameError = _lastNameError.asStateFlow()

        private val _loadingState = MutableStateFlow(false)
        val loadingState = _loadingState.asStateFlow()

        private val _unknownErrorState = MutableStateFlow<Boolean>(false)
        val unknownErrorState = _unknownErrorState.asStateFlow()

        fun onFirstNameChange(
            context: Context,
            firstName: String,
        ) {
            if (firstName.isEmpty()) {
                _firstNameError.value = InputError(context.getString(R.string.mandatory_field_supporting_text))
            } else {
                _firstNameError.value = null
            }
            _firstName.value = firstName
        }

        fun onLastNameChange(
            context: Context,
            lastName: String,
        ) {
            if (lastName.isEmpty()) {
                _lastNameError.value = InputError(context.getString(R.string.mandatory_field_supporting_text))
            } else {
                _lastNameError.value = null
            }
            _lastName.value = lastName
        }

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

        fun onPasswordChange(
            context: Context,
            password: String,
        ) {
            if (password.length < 6) {
                _passwordError.value = InputError(context.getString(R.string.password_too_short_supporting_text))
            } else {
                _passwordError.value = null
            }
            _password.value = password
        }

        fun onConfirmPasswordChange(confirmPassword: String) {
            _confirmPassword.value = confirmPassword
        }

        fun dismissUnknownErrorDialog() {
            _unknownErrorState.value = false
        }

        fun redirectLogin() {
            navigateLogin()
        }

        fun signUpWithEmailAndPassword(
            context: Context,
            email: String,
            password: String,
            confirmPassword: String,
            firstName: String,
            lastName: String,
        ) {
            if (firstName.isEmpty()) {
                _firstNameError.value = InputError(context.getString(R.string.mandatory_field_supporting_text))
            } else {
                _firstNameError.value = null
            }

            if (lastName.isEmpty()) {
                _lastNameError.value = InputError(context.getString(R.string.mandatory_field_supporting_text))
            } else {
                _lastNameError.value = null
            }

            if (password != confirmPassword) {
                _passwordError.value = InputError(context.getString(R.string.passwords_not_match_supporting_text))
                _confirmPasswordError.value = InputError(context.getString(R.string.passwords_not_match_supporting_text))
                return
            } else {
                _passwordError.value = null
                _confirmPasswordError.value = null
            }
            viewModelScope.launch {
                try {
                    _loadingState.value = true
                    authService.signUp(email, password, firstName, lastName).collect {
                            success ->
                        if (success) {
                            _loadingState.value = false
                            navigateSuccess()
                        }
                    }
                } catch (e: AuthenticationException) {
                    Log.i("SignUpViewModel", e::class.toString())
                    e.message?.let { Log.e("SignUpViewModel", it) }
                    when (e) {
                        is AuthenticationException.InvalidEmailException -> {
                            _emailError.value = InputError(context.getString(R.string.invalid_email_supporting_text))
                        }
                        is AuthenticationException.InvalidPasswordException, is AuthenticationException.WeakPasswordException -> {
                            _passwordError.value = InputError(context.getString(R.string.password_too_short_supporting_text))
                        }
                        else -> {
                            _emailError.value = null
                            _passwordError.value = null
                            _unknownErrorState.value = true
                        }
                    }
                } catch (e: Exception) {
                    Log.e("LoginViewModel", e.localizedMessage ?: "Erro desconhecido")
                    _unknownErrorState.value = true
                } finally {
                    _loadingState.value = false
                }
            }
        }

        @AssistedFactory
        interface Factory {
            fun create(
                @Assisted("navigateLogin") navigateLogin: () -> Unit,
                @Assisted("navigateSuccess") navigateSuccess: () -> Unit,
            ): SignUpViewModel
        }
    }
