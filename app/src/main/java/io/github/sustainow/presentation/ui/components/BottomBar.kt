package io.github.sustainow.presentation.ui.components

import Home
import Login
import SignUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import io.github.sustainow.routes.routes

@Composable

fun BottomBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
    ) {
        routes().forEachIndexed { num, route ->
            NavigationBarItem(
                icon = {
                    Icon(
                        route.icon,
                        contentDescription = route.name,
                    )
                },
                label = {
                    Text(route.name)
                },
                // if the graph base route is anywhere in the current hierarchy
                selected =
                currentDestination?.hierarchy?.any {
                    it.hasRoute(route.content::class)
                } == true,
                colors =
                NavigationBarItemColors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    selectedIndicatorColor = MaterialTheme.colorScheme.surface,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                onClick = {
                    navController.navigate(route.content)
                },
            )
        }
    }
}