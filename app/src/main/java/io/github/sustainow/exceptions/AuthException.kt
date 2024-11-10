package io.github.sustainow.exceptions

sealed class AuthenticationException(message: String, cause: Throwable) : Exception(message, cause) {
    data class InvalidEmailException(override val message: String, override val cause: Throwable) : AuthenticationException(message, cause)

    data class InvalidPasswordException(override val message: String, override val cause: Throwable) : AuthenticationException(
        message,
        cause,
    ) {
        constructor(message: String) : this(message, Throwable())
    }

    data class WeakPasswordException(override val message: String, override val cause: Throwable) : AuthenticationException(
        message,
        cause,
    )

    data class TooManyRequestsException(override val message: String, override val cause: Throwable) : AuthenticationException(
        message,
        cause,
    )

    data class UnknownException(override val message: String, override val cause: Throwable) : AuthenticationException(
        message,
        cause,
    )
}
