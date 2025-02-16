package io.github.sustainow.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sustainow.domain.model.ConsumptionTotal
import io.github.sustainow.domain.model.Formulary
import io.github.sustainow.domain.model.FormularyAnswer
import io.github.sustainow.domain.model.FormularyAnswerCreate
import io.github.sustainow.domain.model.Question
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.presentation.ui.utils.DataError
import io.github.sustainow.presentation.ui.utils.DataOperation
import io.github.sustainow.presentation.ui.utils.getFirstDayOfCurrentMonth
import io.github.sustainow.presentation.ui.utils.getFirstDayOfPreviousMonth
import io.github.sustainow.presentation.ui.utils.getLastDayOfCurrentMonth
import io.github.sustainow.repository.formulary.FormularyRepository
import io.github.sustainow.service.auth.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus

@HiltViewModel(assistedFactory = FormularyViewModel.Factory::class)
class FormularyViewModel
    @AssistedInject
    constructor(
        private val repository: FormularyRepository,
        private val authService: AuthService,
        @Assisted("area") private val area: String,
        @Assisted("type") private val type: String,
    ) : ViewModel() {
        private val _formulary = MutableStateFlow<Formulary?>(null)
        val formulary = _formulary.asStateFlow()

        private val _currentQuestion = MutableStateFlow<Question?>(null)
        val currentQuestion = _currentQuestion.asStateFlow()

        private val _totalValue = MutableStateFlow<ConsumptionTotal?>(null)
        val totalValue = _totalValue.asStateFlow()

        private val _previousAnswers = MutableStateFlow<List<FormularyAnswer>>(emptyList())
        val previousAnswers = _previousAnswers.asStateFlow()

        private val _selectedAnswers = mutableStateMapOf<Question, List<FormularyAnswerCreate>>()
        val selectedAnswers = _selectedAnswers

        private val _loading = MutableStateFlow(false)
        val loading = _loading.asStateFlow()

        private val _answeredThisMonth = MutableStateFlow<List<FormularyAnswerCreate>>(emptyList())

        private val _showReuseAnswersDialog = MutableStateFlow(false)
        val showReuseAnswersDialog = _showReuseAnswersDialog.asStateFlow()

        private val _error = MutableStateFlow<DataError?>(null)
        val error = _error.asStateFlow()

        private val _success = MutableStateFlow(false)
        val success = _success.asStateFlow()

        val userStateLogged = authService.user.value

        init {
            formularyFetch()
        }

        fun formularyFetch() {
            val currentMonthStart = getFirstDayOfCurrentMonth()
            val currentMonthEnd = getLastDayOfCurrentMonth()

            viewModelScope.launch {
                _loading.value = true
                try {
                    _answeredThisMonth.value =
                        repository.reuseAnswered(
                            area,
                            type,
                            currentMonthStart,
                            currentMonthEnd,
                        )
                    if (_answeredThisMonth.value.isNotEmpty()) {
                        _showReuseAnswersDialog.value = true
                    }

                    _formulary.value = repository.getFormulary(area, type)
                    _currentQuestion.value = formulary.value?.questions?.get(0)
                    _error.value = null
                } catch (e: Exception) {
                    Log.e("exception", "${e.message}", e)
                    _error.value = DataError(source = "formulary", operation = DataOperation.GET)
                } finally {
                    _loading.value = false
                }
            }
        }

        fun goToNextQuestion() {
            Log.i("question", "${currentQuestion.value}")
            val currentId = currentQuestion.value?.id
            if (currentId == null) {
                // Se a pergunta atual não está definida, defina a primeira pergunta
                _currentQuestion.value = formulary.value?.questions?.firstOrNull()
            } else {
                // Encontra a próxima pergunta com ID maior
                val nextQuestion =
                    formulary.value
                        ?.questions
                        ?.filter { it.id!! > currentId }
                        ?.minByOrNull { it.id!! } // Garante pegar a menor ID maior
                if (nextQuestion != null) {
                    _currentQuestion.value = nextQuestion
                } else {
                    Log.i("FormularyViewModel", "No more questions available.")
                }
            }
        }

        fun goToPreviousQuestion() {
            _error.value = null
            Log.i("question", "${currentQuestion.value}")
            if (currentQuestion.value?.id == null) {
                Log.i("currentValue", "${currentQuestion.value}")
                _error.value = DataError(source = "question", operation = DataOperation.GET)
            }
            if (currentQuestion.value == formulary.value?.questions?.first()) {
                return
            }
            currentQuestion.value?.id?.let {
                val previousQuestion = formulary.value?.questions?.find { question -> question.id == it - 1 }
                _currentQuestion.value = previousQuestion
            }
        }

        private suspend fun calculateTotalValue(answers: Iterable<FormularyAnswer>) {
            try {
                val currentUserState = authService.user.value

                if (currentUserState is UserState.Logged) {
                    _totalValue.value = repository.getTotal(answers)
                    _error.value = null
                }
            } catch (e: Exception) {
                Log.e("exception", "${e.message}", e)
                _error.value = DataError(source = "answers", operation = DataOperation.CREATE)
            }
        }

        fun sendAnswers() {
            viewModelScope.launch {
                _loading.value = true
                try {
                    if (formulary.value?.id == null) {
                        throw IllegalArgumentException("Form id is null")
                    }

                    val currentUserState = authService.user.value
                    if (currentUserState is UserState.Logged) {
                        val answers =
                            repository.addAnswers(
                                selectedAnswers.values.flatten(),
                                currentUserState.user.uid,
                                formulary.value!!.id!!,
                            )

                        calculateTotalValue(answers)

                        _success.value = true // Definindo como true após sucesso
                        _error.value = null
                    } else {
                        _error.value = DataError(source = "user", operation = DataOperation.CREATE)
                    }
                } catch (e: Exception) {
                    Log.e("exception", "${e.message}", e)
                    _error.value = DataError(source = "answers", operation = DataOperation.CREATE)
                } finally {
                    _loading.value = false
                }
            }
        }

        fun addAnswerToQuestion(
            question: Question,
            selectedAlternative: FormularyAnswerCreate,
        ) {
            val currentAnswers = selectedAnswers[question] ?: emptyList()
            val updateAnswer = question.onAnswerAdded(selectedAlternative, currentAnswers)

            _selectedAnswers[question] = updateAnswer
        }

        fun onAnswerRemoved(
            question: Question,
            selectedAlternative: FormularyAnswerCreate,
        ) {
            val currentAnswers = selectedAnswers[question] ?: emptyList()
            val updateAnswers = question.onAnswerRemoved(selectedAlternative, currentAnswers)

            _selectedAnswers[question] = updateAnswers
        }

        /**
         * Returns answers from the previous month
         */
        fun getPreviousAnswers() {
            val previousMonthStart = getFirstDayOfPreviousMonth()
            val previousMonthEnd = getFirstDayOfCurrentMonth().minus(1, DateTimeUnit.DAY)

            viewModelScope.launch {
                _loading.value = true
                try {
                    if (userStateLogged is UserState.Logged) {
                        val answers =
                            repository.getAnswered(
                                area,
                                type,
                                previousMonthStart,
                                previousMonthEnd,
                            )

                        _previousAnswers.value = answers
                    }
                } catch (e: Exception) {
                    Log.e("FormularyViewModel", e.message, e)
                    _error.value = DataError(source = "answers", operation = DataOperation.CREATE)
                } finally {
                    _loading.value = false
                }
            }
        }

        /**
         * preset the current answers to the answers given before on the same month
         */
        fun reuseCurrentAnswers() {
            for (question in formulary.value?.questions!!) {
                val answersForQuestion = _answeredThisMonth.value.filter { it.questionId == question.id }
                _selectedAnswers[question] = answersForQuestion
            }
        }

        fun hideReuseAnswersDialog() {
            _showReuseAnswersDialog.value = false
        }

        companion object {
            @Suppress("UNCHECKED_CAST")
            fun factory(
                factory: Factory,
                area: String,
                type: String,
            ): ViewModelProvider.Factory =
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T = factory.create(area, type) as T
                }
        }

        @AssistedFactory
        interface Factory {
            fun create(
                @Assisted("area") area: String,
                @Assisted("type") type: String,
            ): FormularyViewModel
        }
    }
