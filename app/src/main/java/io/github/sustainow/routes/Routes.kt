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
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.github.sustainow.R
import io.github.sustainow.presentation.ui.utils.Route

@Composable
fun routes(): List<Route> {
    val routes =
        listOf(
            Route(stringResource(R.string.home_route_text), Home, Icons.Default.Home),

            Route(stringResource(R.string.consume_route_text), Consume, Icons.Default.VolunteerActivism),
            Route(stringResource(R.string.colective_actions_route_text), CollectiveActions, Icons.Default.Groups3),
            Route(stringResource(R.string.routines_route_text), Routines, Icons.Default.Today),
            Route(stringResource(R.string.historic_route_text), Historic, Icons.Default.History),
        )
    return routes
}
