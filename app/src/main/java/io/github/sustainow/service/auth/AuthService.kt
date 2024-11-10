package io.github.sustainow.service.auth

import io.github.sustainow.domain.model.UserState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

abstract class AuthService {
    abstract var user: MutableStateFlow<UserState>

    /**
     * Sign in with email and password.
     */
    abstract suspend fun signIn(
        email: String,
        password: String,
    ): Flow<Boolean>

    /**
     * Creates a new account.
     */
    abstract suspend fun signUp(
        email: String,
        password: String,
    ): Flow<Boolean>

    /**
     * Sign out the current user.
     */
    abstract suspend fun signOut()

    /**
     * Check if a user is logged in.
     */
    fun isUserLoggedIn(): Boolean {
        return user.value is UserState.Logged
    }
}
