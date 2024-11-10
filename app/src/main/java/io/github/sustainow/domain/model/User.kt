package io.github.sustainow.domain.model

data class User(
    val uid: String,
    val name: String,
    val email: String,
    val profilePicture: String?,
)

sealed class UserState {
    data object Loading : UserState()

    data class Logged(val user: User) : UserState()

    data object NotLogged : UserState()
}
