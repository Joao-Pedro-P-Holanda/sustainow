package io.github.sustainow.presentation.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.Module
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sustainow.domain.model.Formulary
import io.github.sustainow.domain.model.FormularyAnswer
import io.github.sustainow.domain.model.Question
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.presentation.ui.utils.DataError
import io.github.sustainow.presentation.ui.utils.DataOperation
import io.github.sustainow.presentation.ui.utils.getCurrentDate
import io.github.sustainow.repository.formulary.FormularyRepository
import io.github.sustainow.service.auth.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.minus
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.temporal.TemporalAdjusters

@RequiresApi(Build.VERSION_CODES.O)
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

        private val _selectedAnswers = mutableStateMapOf<Question, List<FormularyAnswer>>()
        val selectedAnswers = _selectedAnswers

        private val _loading = MutableStateFlow(false)
        val loading = _loading.asStateFlow()

        private val _error = MutableStateFlow<DataError?>(null)
        val error = _error.asStateFlow()

        private val _success = MutableStateFlow(false)
        val success = _success.asStateFlow()

        val userStateLogged = authService.user.value

        init {
            formularyFetch()
            getPreviousAnswers()
        }

        fun formularyFetch() {
            viewModelScope.launch {
                _loading.value = true
                try {
                    _formulary.value = repository.getFormulary(area, type)
                    _currentQuestion.value = formulary.value?.questions?.get(0)
                    _error.value = null
                } catch (e: Exception) {
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
            val nextQuestion = formulary.value?.questions
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

        fun calculateTotalValue(): Float {
            var total = 0f
            Log.i("viewModel", "${_selectedAnswers[_currentQuestion.value]}")
            for (answers in _selectedAnswers.values) {
                answers.forEach{ total += it.value }
            }
            return total
        }

        fun sendAnswers() {
            Log.i("viewModel", "${_selectedAnswers[_currentQuestion.value]}")
            viewModelScope.launch {
                _loading.value = true
                try {
                    val currentUserState = authService.user.value
                    if (currentUserState is UserState.Logged) {
                        val values: MutableList<FormularyAnswer> = emptyList<FormularyAnswer>().toMutableList()

                        for (answers in _selectedAnswers.values){
                            answers.forEach{ values += it}
                        }
                        repository.addAnswers(values, currentUserState.user.uid)
                        _success.value = true // Definindo como true após sucesso
                        _error.value = null
                    } else {
                        _error.value = DataError(source = "user", operation = DataOperation.CREATE)
                    }
                } catch (e: Exception) {
                    _error.value = DataError(source = "answers", operation = DataOperation.CREATE)
                } finally {
                    _loading.value = false
                }
            }
        }

        fun addAnswerToQuestion(
            question: Question,
            selectedAlternative: FormularyAnswer,
        ) {
            val currentAnswers = selectedAnswers[question] ?: emptyList()
            val updateAnswer = question.onAnswerAdded(selectedAlternative, currentAnswers)

            _selectedAnswers[question] = updateAnswer
        }

        fun onAnswerRemoved(
            question: Question,
            selectedAlternative: FormularyAnswer
        ){
            val currentAnswers = selectedAnswers[question] ?: emptyList()
            val updateAnswers = question.onAnswerRemoved(selectedAlternative, currentAnswers)

            _selectedAnswers[question] = updateAnswers
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getPreviousAnswers() {
            val previousMonthDate = (getCurrentDate() - DatePeriod(months = 1)).toJavaLocalDate()
            val previousMonthStart = previousMonthDate.with(TemporalAdjusters.firstDayOfMonth())
            val previousMonthEnd = previousMonthDate.with(TemporalAdjusters.lastDayOfMonth())

            viewModelScope.launch {
                _loading.value = true
                try {
                    if (userStateLogged is UserState.Logged) {
                        val answers =
                            repository.getAnswered(
                                area,
                                userStateLogged.user.uid,
                                previousMonthStart.toKotlinLocalDate(),
                                previousMonthEnd.toKotlinLocalDate(),
                            )

                        _currentQuestion.value?.let { currentQuestion ->
                            _selectedAnswers[currentQuestion] = answers
                        }
                    }
                } catch (e: Exception) {
                    Log.e("FormularyViewModel", e.message, e)
                    _error.value = DataError(source = "answers", operation = DataOperation.CREATE)
                } finally {
                    _loading.value = false
                }
            }
        }

        companion object {
            @Suppress("UNCHECKED_CAST")
            fun factory(
                factory: Factory,
                area: String,
                type: String,
            ): ViewModelProvider.Factory {
                return object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T = factory.create(area, type) as T
                }
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

@Module
@InstallIn(ActivityRetainedComponent::class)
interface AssistedInjectModule
