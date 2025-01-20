package io.github.sustainow.routes

import CollectiveActions
import Consume
import Home
import Routines
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups3
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.VolunteerActivism
import io.github.sustainow.presentation.ui.utils.Route

fun routes(): List<Route> {
    return listOf(
        Route("Home", Home, Icons.Default.Home),
        Route("Consumo", Consume, Icons.Default.VolunteerActivism),
        Route("Ações", CollectiveActions, Icons.Default.Groups3),
        Route("Rotinas", Routines, Icons.Default.Today),
        Route("Histórico", Historic, Icons.Default.History),
    )
}
