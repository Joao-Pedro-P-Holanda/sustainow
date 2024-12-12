package io.github.sustainow.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sustainow.domain.model.Formulary
import io.github.sustainow.domain.model.FormularyAnswer
import io.github.sustainow.domain.model.Question
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.repository.formulary.FormularyRepository
import io.github.sustainow.service.auth.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val authService: AuthService,
        // TODO: Remove repository after manual testing
        private val formularyRepository: FormularyRepository,
    ) : ViewModel() {
        private val _displayFormulary = MutableStateFlow<Formulary?>(null)
        val displayFormulary = _displayFormulary.asStateFlow()
        private val _displayQuestions = MutableStateFlow<List<Question>?>(null)
        val displayQuestions = _displayQuestions.asStateFlow()
        private val _displayAnswers = MutableStateFlow<List<FormularyAnswer>?>(null)
        val displayAnswers = _displayAnswers.asStateFlow()

        private val currentUser = authService.user.asStateFlow()

        fun signOut() {
            viewModelScope.launch {
                authService.signOut()
            }
        }

        fun getFormulary(area: String) {
            viewModelScope.launch {
                _displayFormulary.value = formularyRepository.getFormulary(area)
            }
        }

        fun getQuestions(area: String) {
            viewModelScope.launch {
                _displayQuestions.value = formularyRepository.getFormulary(area).questions
            }
        }

        fun addAnswers(answers: List<FormularyAnswer>) {
            viewModelScope.launch {
                if (currentUser.value is UserState.Logged) {
                    formularyRepository.addAnswers(
                        answers,
                        (currentUser.value as UserState.Logged).user.uid,
                    )
                }
            }
        }

        fun getAnswers(area: String) {
            viewModelScope.launch {
                val startDate = LocalDate(2021, 10, 1)
                val endDate = LocalDate(2021, 10, 31)
                if (currentUser.value is UserState.Logged) {
                    _displayAnswers.value =
                        formularyRepository.getAnswered(area, (currentUser.value as UserState.Logged).user.uid, startDate, endDate)
                }
            }
        }
    }
