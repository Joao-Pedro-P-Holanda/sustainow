package io.github.sustainow.presentation.viewmodel

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sustainow.domain.model.FormularyAnswer
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.presentation.ui.utils.DataError
import io.github.sustainow.presentation.ui.utils.DataOperation
import io.github.sustainow.repository.formulary.FormularyRepository
import io.github.sustainow.service.auth.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@HiltViewModel(assistedFactory = HistoricViewModel.Factory::class)
class HistoricViewModel
@AssistedInject
constructor(
    private val repository: FormularyRepository,
    private val authService: AuthService,
    @Assisted("area") private val area: String,
) : ViewModel() {
    private val _formulary = MutableStateFlow<List<FormularyAnswer>?>(null)
    val formulary = _formulary.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<DataError?>(null)
    val error = _error.asStateFlow()

    val userState = authService.user.value

    init {
        val startDate = LocalDate(2021, 1, 1)
        val currentDate = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        formularyFetch(startDate = startDate, endDate = currentDate)
    }

    @OptIn(UnstableApi::class)
    fun formularyFetch(startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch {
            _loading.value = true
            try {
                if(userState is UserState.Logged) {
                    _formulary.value = repository.getAnswered(area, userState.user.uid, startDate, endDate)
                }
                _error.value = null
            } catch (e: Exception) {
                _error.value = DataError(source = "formulary", operation = DataOperation.GET)
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
                override fun <T : ViewModel> create(modelClass: Class<T>): T = factory.create(area) as T
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("area") area: String,
        ): HistoricViewModel
    }
}
