package io.github.sustainow.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Modal that shows a loading spinner in the center of the screen and a dimmed background.
 */
@Composable
fun LoadingModal(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.4f))
    ) {
        // Centered loading spinner
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
        )
    }
}
