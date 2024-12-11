package io.github.sustainow.exceptions

open class ConnectionException(message: String, cause: Throwable) : Exception(message, cause)

data class ResponseException(override val message: String, override val cause: Throwable) : ConnectionException(message, cause)

data class TimeoutException(override val message: String, override val cause: Throwable) : ConnectionException(message, cause)

data class UnknownException(override val message: String, override val cause: Throwable) : ConnectionException(message, cause)
