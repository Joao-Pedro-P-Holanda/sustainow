package io.github.sustainow.service.auth

import android.util.Log
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.exception.AuthErrorCode
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.sustainow.domain.model.User
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.exceptions.AuthenticationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import javax.inject.Inject

class AuthServiceSupabaseImp
    @Inject
    constructor(private val auth: Auth) : AuthService() {
        override var user: MutableStateFlow<UserState> = MutableStateFlow(UserState.Loading)
        private val scope = CoroutineScope(Dispatchers.IO)

        init {
            observeSessionChanges()
        }

        override suspend fun signIn(
            email: String,
            password: String,
        ): Flow<Boolean> =
            callbackFlow {
                try {
                    auth.signInWith(Email) {
                        this.email = email
                        this.password = password
                    }
                } catch (e: AuthRestException) {
                    e.message?.let { Log.e("AuthServiceSupabaseImp", it) }
                    when (e.errorCode) {
                        AuthErrorCode.EmailAddressNotAuthorized, AuthErrorCode.EmailExists, AuthErrorCode.UserAlreadyExists -> throw AuthenticationException.InvalidEmailException(
                            e.message ?: "Invalid email",
                            e,
                        )
                        AuthErrorCode.InvalidCredentials -> throw AuthenticationException.InvalidPasswordException(
                            e.message ?: "Wrong password",
                            e,
                        )
                        AuthErrorCode.OverRequestRateLimit -> throw AuthenticationException.TooManyRequestsException(
                            e.message ?: "Too many requests",
                            e,
                        )
                        else -> throw AuthenticationException.UnknownException(e.message ?: "Unknown error", e)
                    }
                } catch (e: Exception) {
                    throw AuthenticationException.UnknownException(e.message ?: "Unknown error", e)
                }
                trySend(true)
                awaitClose()
            }

        override suspend fun signUp(
            email: String,
            password: String,
            firstName: String,
            lastName: String,
        ): Flow<Boolean> =
            callbackFlow {
                try {
                    if (firstName.isEmpty() || lastName.isEmpty()) {
                        throw IllegalArgumentException("First name and last name cannot be empty")
                    }
                    auth.signUpWith(Email) {
                        this.email = email
                        this.password = password
                        data =
                            buildJsonObject {
                                put("first_name", firstName)
                                put("last_name", lastName)
                            }
                    }
                } catch (e: AuthRestException) {
                    e.message?.let { Log.e("AuthServiceSupabaseImp", it) }
                    when (e.errorCode) {
                        AuthErrorCode.EmailExists, AuthErrorCode.UserAlreadyExists -> throw AuthenticationException.InvalidEmailException(
                            e.message ?: "Email already exists",
                            e,
                        )
                        AuthErrorCode.EmailAddressNotAuthorized -> throw AuthenticationException.InvalidEmailException(
                            e.message ?: "Invalid email",
                            e,
                        )
                        AuthErrorCode.WeakPassword -> throw AuthenticationException.WeakPasswordException(
                            e.message ?: "Weak password",
                            e,
                        )
                        AuthErrorCode.OverRequestRateLimit -> throw AuthenticationException.TooManyRequestsException(
                            e.message ?: "Too many requests",
                            e,
                        )
                        else -> throw AuthenticationException.UnknownException(e.message ?: "Unknown error", e)
                    }
                } catch (e: Exception) {
                    throw AuthenticationException.UnknownException(e.message ?: "Unknown error", e)
                }
                trySend(true)
                awaitClose()
            }

        private fun observeSessionChanges() {
            scope.launch {
                auth.sessionStatus.collect {
                    when (it) {
                        is SessionStatus.Authenticated -> {
                            val newUser = it.session.user
                            user.value =
                                UserState.Logged(
                                    User(
                                        newUser!!.id,
                                        newUser.userMetadata?.get("first_name")?.jsonPrimitive?.content ?: "",
                                        newUser.userMetadata?.get("last_name")?.jsonPrimitive?.content ?: "",
                                        newUser.email ?: "",
                                        newUser.userMetadata?.get("profile_pic")?.jsonPrimitive?.content,
                                    ),
                                )
                        }
                        SessionStatus.Initializing -> user.value = UserState.Loading
                        is SessionStatus.RefreshFailure, is SessionStatus.NotAuthenticated -> user.value = UserState.NotLogged
                    }
                }
            }
        }

        override suspend fun signOut() {
            auth.signOut()
        }
    }
