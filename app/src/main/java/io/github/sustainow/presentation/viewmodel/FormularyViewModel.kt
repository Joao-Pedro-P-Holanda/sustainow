package io.github.sustainow.presentation.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import io.github.sustainow.domain.model.QuestionAlternative
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.presentation.ui.utils.DataError
import io.github.sustainow.presentation.ui.utils.DataOperation
import io.github.sustainow.presentation.ui.utils.getCurrentDate
import io.github.sustainow.repository.formulary.FormularyRepository
import io.github.sustainow.service.auth.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
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

        private val _currentAnswers = MutableStateFlow<List<FormularyAnswer>>(emptyList())
        val currentAnswers = _currentAnswers.asStateFlow()

        private val _previousAnswers = MutableStateFlow<List<FormularyAnswer>>(emptyList())
        val previousAnswers = _previousAnswers.asStateFlow()

        private val _loading = MutableStateFlow(false)
        val loading = _loading.asStateFlow()
        private val _error = MutableStateFlow<DataError?>(null)
        val error = _error.asStateFlow()

        // Novo estado de sucesso
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
            Log.i("viewModel", "${_currentAnswers.value}")
            for (answer in _currentAnswers.value) {
                total += answer.value
            }
            return total
        }

        fun sendAnswers() {
            Log.i("viewModel", "${_currentAnswers.value}")
            viewModelScope.launch {
                _loading.value = true
                try {
                    val currentUserState = authService.user.value
                    if (currentUserState is UserState.Logged) {
                        repository.addAnswers(currentAnswers.value ?: emptyList(), currentUserState.user.uid)
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
            selectedAlternative: QuestionAlternative,
            formId: Int?,
            uid: String,
            groupName: String?,
            month: Int,
        ) {
            // Acessa a lista de respostas atual sem criar uma nova
            val existingAnswers = _currentAnswers.value

            when (question) {
                is Question.SingleSelect -> {
                    // Substituir a resposta existente'
                    _currentAnswers.value = existingAnswers.filter { it.questionId != question.id } +
                        FormularyAnswer(
                            formId = formId,
                            uid = uid,
                            groupName = "",
                            questionId = question.id,
                            value = selectedAlternative.value,
                            timePeriod = selectedAlternative.timePeriod,
                            unit = selectedAlternative.unit,
                            month = month,
                        )
                    Log.i("viewModel", "${_currentAnswers.value}")
                }

                is Question.Numerical -> {
                    // Atualizar a resposta existente ou adicionar nova
                    _currentAnswers.value = existingAnswers.filter { it.questionId != question.id } +
                        FormularyAnswer(
                            formId = formId,
                            uid = uid,
                            groupName = "",
                            questionId = question.id,
                            value = selectedAlternative.value,
                            timePeriod = selectedAlternative.timePeriod,
                            unit = selectedAlternative.unit,
                            month = month,
                        )
                }

                is Question.MultiSelect -> {
                    // Adicionar ou remover a resposta, dependendo se já existe
                    val updatedAnswers =
                        if (existingAnswers.any { it.questionId == question.id && it.value == selectedAlternative.value }) {
                            // Se já existe, remover a resposta
                            existingAnswers.filter { it.questionId != question.id || it.value != selectedAlternative.value }
                        } else {
                            // Caso contrário, adicionar a nova resposta
                            existingAnswers +
                                FormularyAnswer(
                                    formId = formId,
                                    uid = uid,
                                    groupName = "",
                                    questionId = question.id,
                                    value = selectedAlternative.value,
                                    timePeriod = selectedAlternative.timePeriod,
                                    unit = selectedAlternative.unit,
                                    month = month,
                                )
                        }
                    _currentAnswers.value = updatedAnswers
                }

                is Question.MultiItem -> {
                    // Adicionar novas respostas com groupName
                    _currentAnswers.value = existingAnswers +
                        FormularyAnswer(
                            formId = formId,
                            uid = uid,
                            groupName = groupName ?: "", // Se groupName for nulo, passar uma string vazia
                            questionId = question.id,
                            value = selectedAlternative.value,
                            timePeriod = selectedAlternative.timePeriod,
                            unit = selectedAlternative.unit,
                            month = month,
                        )
                }
            }
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
                        _previousAnswers.value =
                            repository.getAnswered(
                                area,
                                userStateLogged.user.uid,
                                previousMonthStart.toKotlinLocalDate(),
                                previousMonthEnd.toKotlinLocalDate(),
                            )
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
