package io.github.sustainow.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sustainow.domain.model.CollectiveAction
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.repository.actions.CollectiveActionRepository
import io.github.sustainow.service.auth.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate


@HiltViewModel(assistedFactory = CollectiveActionViewModel.Factory::class)
class CollectiveActionViewModel @AssistedInject constructor(private val authService: AuthService,
                                                            private val repository: CollectiveActionRepository,
                                                            @Assisted("id") private val id:Int?,
                                                            @Assisted("deleteCallback") private val deleteCallback: ()->Unit,
                                                            @Assisted("successCreateCallback") private val sucessCreateCallback:(()->Unit)?,
                                                            @Assisted("successUpdateCallback") private val sucessUpdateCallback: (() -> Unit)?) : ViewModel(){
    private val _action = MutableStateFlow<CollectiveAction?>(null)
    val action = _action.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        fetch()
    }

    fun fetch() {
        viewModelScope.launch {
            _loading.value = true
            try {
                if (id != null) {
                    _action.value = repository.get(id)
                }
            } catch (e: Exception) {
                _error.value = "Erro ao buscar ação coletiva"
            } finally {
                _loading.value = false
            }
        }
    }

    fun create(
        name:String,
        images:List<Uri>,
        description:String,
        status: String,
        startDate:LocalDate,
        endDate:LocalDate,

    ){
        viewModelScope.launch{
            _loading.value=true
            try{
                val currentUserState = authService.user.value
                if(currentUserState is UserState.Logged) {
                    repository.create(
                        CollectiveAction(
                            id=null,
                            name = name,
                            images = images,
                            description = description,
                            status = status,
                            authorId = currentUserState.user.uid,
                            authorName = null,
                            startDate = startDate,
                            endDate = endDate,
                        )
                    )
                    sucessCreateCallback?.invoke()
                }
            } catch (e: Exception) {
                Log.e("CollectiveActionViewModel", "Error creating collective action", e)
                _error.value = "Erro ao criar ação coletiva"
            } finally {
                _loading.value = false
            }

        }
    }


    fun update(
        name:String,
        images:List<Uri>,
        description:String,
        status: String,
        startDate:LocalDate,
        endDate:LocalDate,
    ){
        viewModelScope.launch{
           _loading.value=true
            try{
                if(_action.value!=null){
                   repository.update(
                       CollectiveAction(
                           id = _action.value!!.id,
                           name = name,
                           images = images,
                           description = description,
                           status = status,
                           authorId = _action.value!!.authorId,
                           authorName = _action.value!!.authorName,
                           startDate = startDate,
                           endDate = endDate,
                       )
                   )
                    sucessUpdateCallback?.invoke()
                }
            } catch (e: Exception) {
                _error.value = "Erro ao atualizar ação coletiva"
                Log.e("CollectiveActionViewModel", "Error updating collective action", e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun delete( ){
        viewModelScope.launch{
            _loading.value=true
            try{
                if(id!=null){
                   repository.delete(id)
                    deleteCallback()
                }
            } catch (e: Exception) {
                _error.value = "Erro ao deletar ação coletiva"
            } finally {
                _loading.value = false
            }
        }
    }

    @AssistedFactory
        interface Factory {
            fun create(
                @Assisted("id") id: Int?,
                @Assisted("deleteCallback") deleteCallback: ()->Unit,
                @Assisted("successCreateCallback") successCreateCallback: (() -> Unit)?,
                @Assisted("successUpdateCallback") successUpdateCallback: (() -> Unit)?
            ): CollectiveActionViewModel
        }
}