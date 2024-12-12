package io.github.sustainow.presentation.ui.utils

/**
 * Represents an error that occurred while fetching or sending data from any source.
 * Errors created in the viewModel should have their message customized in the view layer.
 */
data class DataError(
    var message: String? = null,
    /*
    the object that caused the error
     */
    val source: Any,
    val operation: DataOperation,
)
