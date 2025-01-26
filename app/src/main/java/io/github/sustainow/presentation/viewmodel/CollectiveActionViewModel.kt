package io.github.sustainow.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sustainow.domain.model.ActivityType
import io.github.sustainow.domain.model.CollectiveAction
import io.github.sustainow.domain.model.Invitation
import io.github.sustainow.domain.model.MemberActivity
import io.github.sustainow.domain.model.MemberActivityCreate
import io.github.sustainow.domain.model.UserProfile
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.repository.actions.CollectiveActionRepository
import io.github.sustainow.service.auth.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@HiltViewModel(assistedFactory = CollectiveActionViewModel.Factory::class)
class CollectiveActionViewModel @AssistedInject constructor(private val authService: AuthService,
                                                            private val repository: CollectiveActionRepository,
                                                            @Assisted("id") private val id:Int?,
                                                            @Assisted("deleteCallback") private val deleteCallback: ()->Unit,
                                                            @Assisted("successCreateCallback") private val sucessCreateCallback:(()->Unit)?,
                                                            @Assisted("successUpdateCallback") private val sucessUpdateCallback: (() -> Unit)?) : ViewModel(){
    private val _action = MutableStateFlow<CollectiveAction?>(null)
    val action = _action.asStateFlow()

    private val _comment = MutableStateFlow<String>("")
    val comment = _comment.asStateFlow()

    private val _users = MutableStateFlow<List<UserProfile>?>(null)
    val users = _users.asStateFlow()

    private val _activities = MutableStateFlow<List<MemberActivity>?>(null)
    val activities = _activities.asStateFlow()

    // users that were invited but didn't answer yet
    private val _invitations = MutableStateFlow<List<Invitation>?>(null)
    val invitations = _invitations.asStateFlow()

    private val _usersToInvite = MutableStateFlow<Set<UserProfile>>(mutableSetOf())
    val usersToInvite = _usersToInvite.asStateFlow()

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
                    _invitations.value = repository.listActionInvitations(id)
                    _activities.value = repository.listActionActivities(id)
                }
                _users.value = authService.listUsers()
            } catch (e: Exception) {
                _error.value = "Erro ao buscar ação coletiva"
                Log.e("CollectiveActionViewModel", "Error fetching collective action", e)
            } finally {
                _loading.value = false
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun create(
        name:String,
        images:List<Uri>,
        description:String,
        status: String,
        startDate:LocalDate,
        endDate:LocalDate,
        usersToInvite: Iterable<UserProfile>
    ){
        viewModelScope.launch{
            _loading.value=true
            try{
                val currentUserState = authService.user.value
                if(currentUserState is UserState.Logged) {
                    val response =repository.create(
                        CollectiveAction(
                            id=null,
                            name = name,
                            images = images,
                            description = description,
                            status = status,
                            authorId = Uuid.parse(currentUserState.user.uid),
                            authorName = null,
                            startDate = startDate,
                            endDate = endDate,
                            members = mutableListOf()
                        )
                    )
                    repository.inviteMembers(response.id!!, usersToInvite)
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


    @OptIn(ExperimentalUuidApi::class)
    fun update(
        name:String,
        images:List<Uri>,
        description:String,
        status: String,
        startDate:LocalDate,
        endDate:LocalDate,
        usersToInvite: Iterable<UserProfile>
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
                           members = _action.value!!.members
                       )
                   )
                    repository.inviteMembers(_action.value!!.id!!, usersToInvite)
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

    fun addUserToInvites(user:UserProfile){
            val newMembers= _usersToInvite.value.toMutableSet()
            newMembers.add(user)
            _usersToInvite.value = newMembers
    }

    fun removeUserFromInvites(user:UserProfile){
        val newMembers= _usersToInvite.value.toMutableSet()
        newMembers.remove(user)
        _usersToInvite.value = newMembers
    }

    fun sendComment(){
        viewModelScope.launch {
        try {
            val currentUserState = authService.user.value
            if (currentUserState is UserState.Logged) {
                repository.addComment(
                    MemberActivityCreate(
                        memberId = currentUserState.user.uid,
                        actionId = _action.value!!.id!!,
                        type = ActivityType.COMMENT,
                        comment = _comment.value
                    )
                )
                _activities.value = repository.listActionActivities(_action.value!!.id!!)
            }
        }
            catch (e: Exception){
                _error.value = "Erro ao enviar comentário"
                Log.e("CollectiveActionViewModel", "Error sending comment", e)
            }
        }
    }
    fun removeComment(id:Int){
        viewModelScope.launch {
            try {
                val currentUserState = authService.user.value
                if (currentUserState is UserState.Logged) {
                    repository.removeComment(
                        id
                    )
                    _activities.value = repository.listActionActivities(_action.value!!.id!!)
                }
            }
            catch (e: Exception){
                _error.value = "Erro ao remover comentário"
                Log.e("CollectiveActionViewModel", "Error removing comment", e)
            }
        }
    }

    fun setComment(text: String){
        if(text.length<180) {
            _comment.value = text
        }
    }

    fun setError(text: String){
        _error.value = text
    }

    fun resetError(){
        _error.value = null
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