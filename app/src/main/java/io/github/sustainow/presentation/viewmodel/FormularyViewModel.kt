package io.github.sustainow.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sustainow.domain.model.Formulary
import io.github.sustainow.domain.model.FormularyAnswer
import io.github.sustainow.domain.model.Question
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.presentation.ui.utils.DataError
import io.github.sustainow.presentation.ui.utils.DataOperation
import io.github.sustainow.repository.formulary.FormularyRepository
import io.github.sustainow.service.auth.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = FormularyViewModel.Factory::class)
class FormularyViewModel
    @AssistedInject
    constructor(
        private val repository: FormularyRepository,
        private val authService: AuthService,
        @Assisted("area") private val area: String,
    ) : ViewModel() {
        private val _formulary = MutableStateFlow<Formulary?>(null)
        val formulary = _formulary.asStateFlow()

        private val _currentQuestion = MutableStateFlow<Question?>(null)
        val currentQuestion = _currentQuestion.asStateFlow()

        private val _currentAnswers = MutableStateFlow<List<FormularyAnswer>?>(null)
        val currentAnswers = _currentAnswers.asStateFlow()

        private val _loading = MutableStateFlow(false)
        val loading = _loading.asStateFlow()
        private val _error = MutableStateFlow<DataError?>(null)
        val error = _error.asStateFlow()

        init {
            viewModelScope.launch {
                try {
                    _formulary.value = repository.getFormulary(area)
                } catch (e: Exception) {
                    _error.value = DataError(source = formulary, operation = DataOperation.GET)
                }
            }
        }

        fun retryFormularyFetch() {
            viewModelScope.launch {
                try {
                    _formulary.value = repository.getFormulary(area)
                } catch (e: Exception) {
                    _error.value = DataError(source = formulary, operation = DataOperation.GET)
                }
            }
        }

        fun goToNextQuestion() {
            if (currentQuestion.value == null) {
                _currentQuestion.value = formulary.value?.questions?.first()
            } else if (currentQuestion.value?.id == null) {
                _error.value = DataError(source = currentQuestion, operation = DataOperation.GET)
            } else {
                currentQuestion.value?.id?.let {
                    val nextQuestion =
                        formulary.value?.questions?.find { question -> question.id == it + 1 }
                            ?: currentQuestion.value
                    _currentQuestion.value = nextQuestion
                }
            }
        }

        fun goToPreviousQuestion() {
            if (currentQuestion.value?.id == null) {
                _error.value = DataError(source = currentQuestion, operation = DataOperation.GET)
            }
            if (currentQuestion.value == formulary.value?.questions?.first()) {
                return
            }
            currentQuestion.value?.id?.let {
                val previousQuestion = formulary.value?.questions?.find { question -> question.id == it - 1 }
                _currentQuestion.value = previousQuestion
            }
        }

        fun sendAnswers() {
            viewModelScope.launch {
                _loading.value = true
                try {
                    val currentUserState = authService.user.value
                    if (currentUserState is UserState.Logged) {
                        repository.addAnswers(currentAnswers.value ?: emptyList(), currentUserState.user.uid)
                    } else {
                        _error.value = DataError(source = currentUserState, operation = DataOperation.CREATE)
                    }
                } catch (e: Exception) {
                    _error.value = DataError(source = currentAnswers, operation = DataOperation.CREATE)
                } finally {
                    _loading.value = false
                }
            }
        }

        @AssistedFactory
        interface Factory {
            fun create(
                @Assisted("area") area: String,
            ): FormularyViewModel
        }
    }
