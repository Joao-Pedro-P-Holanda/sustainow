package io.github.sustainow.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sustainow.domain.model.CollectiveAction
import io.github.sustainow.domain.model.Invitation
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.repository.actions.CollectiveActionRepository
import io.github.sustainow.service.auth.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@HiltViewModel
class SearchCollectiveActionsViewModel @Inject constructor(private val repository:CollectiveActionRepository, private val authService: AuthService) : ViewModel() {
    private val currentUserState = authService.user.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _startDate = MutableStateFlow<LocalDate?>(null)
    val startDate = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow<LocalDate?>(null)
    val endDate = _endDate.asStateFlow()

    private val _finished = MutableStateFlow<Boolean?>(null)
    val finished = _finished.asStateFlow()

    private val _ascendingDate = MutableStateFlow(false)
    val ascendingDate = _ascendingDate.asStateFlow()

    private val _collectiveActions = MutableStateFlow<List<CollectiveAction>?>(null)

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _displayCollectiveActions = MutableStateFlow(_collectiveActions.value)
    val collectiveActions = _displayCollectiveActions.asStateFlow()

    private val _invitations = MutableStateFlow<List<Invitation>?>(null)
    val invitations = _invitations.asStateFlow()

    init {
        searchCollectiveActions()
    }


    @OptIn(ExperimentalUuidApi::class)
    fun searchCollectiveActions() {
        viewModelScope.launch {
        _loading.value=true
        try {
            if(currentUserState.value is UserState.Logged) {
                _invitations.value = repository.listPendingInvitations(Uuid.parse((currentUserState.value as UserState.Logged).user.uid))
            }
            _collectiveActions.value = repository.list()
            _displayCollectiveActions.value = _collectiveActions.value
        } catch (e: Exception) {
            Log.e("CollectiveActionViewModel", "Error loading collective actions", e)
        }
        finally {
            _loading.value = false
        }
        }
    }

    fun setSearchText(text: String) {
        _searchText.value = text
    }


    fun setStartDate(date: LocalDate?) {
        _startDate.value = date
        filterCollectiveActions()
    }

    fun setEndDate(date: LocalDate?) {
        _endDate.value = date
        filterCollectiveActions()
    }

    fun setFinished(isFinished: Boolean?) {
        _finished.value = isFinished
        filterCollectiveActions()
    }

    fun reverseAscendingDate() {
        _ascendingDate.value = !_ascendingDate.value
        _displayCollectiveActions.value = _displayCollectiveActions.value?.reversed()
    }

    fun filterCollectiveActions() {
        Log.i("Collective ViewModel", "Filtering actions")
        val filteredList = _collectiveActions.value?.filter { action ->
            val matchesSearchText = _searchText.value.isEmpty() || action.name.contains(_searchText.value, ignoreCase = true)
            val matchesStartDate = _startDate.value == null || action.startDate >= _startDate.value!!
            val matchesEndDate = _endDate.value == null || action.endDate <= _endDate.value!!
            val matchesFinished = _finished.value == null || (action.status == "Finalizada") == _finished.value

            matchesSearchText && matchesStartDate && matchesEndDate && matchesFinished
        }?.sortedBy { action ->
            if (_ascendingDate.value) action.startDate else action.startDate
        }

        _displayCollectiveActions.value = filteredList
    }

    @OptIn(ExperimentalUuidApi::class)
    fun respondInvitation(invitation: Invitation) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.answerInvitation(invitation)
                _invitations.value = repository.listPendingInvitations(Uuid.parse((currentUserState.value as UserState.Logged).user.uid))
            } catch (e: Exception) {
                Log.e("CollectiveActionViewModel", "Error responding invitation", e)
            } finally {
                _loading.value = false
            }
        }
    }
}