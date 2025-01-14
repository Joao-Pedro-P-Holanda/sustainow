package io.github.sustainow.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sustainow.domain.model.CollectiveAction
import io.github.sustainow.repository.actions.CollectiveActionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject


@HiltViewModel
class SearchCollectiveActionsViewModel @Inject constructor(private val repository:CollectiveActionRepository) : ViewModel() {
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

    init {
        searchCollectiveActions()
    }

    fun searchCollectiveActions() {
        viewModelScope.launch {
        _loading.value=true
        try {
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
}