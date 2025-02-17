package io.github.sustainow.presentation.ui.components

import Configuration
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import io.github.sustainow.R
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.service.auth.AuthService
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    authService: AuthService,
    userState: UserState,
    canNavigateBack: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var showUserMenu by remember {
        mutableStateOf(false)
    }

    TopAppBar(
        title = {
            val logoResource =
                painterResource(id = R.drawable.sustainow_logo_transparent)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.weight(1f)) // Adiciona espaço entre o botão de voltar e a logo
                // Tornar a logo clicável
                Image(
                    logoResource,
                    contentDescription = null,
                    modifier = Modifier.requiredSize(150.dp, 150.dp),
                )
                Spacer(modifier = Modifier.weight(1f)) // Centraliza a logo
            }
        },
        colors =
        TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = context.getString(R.string.back),
                    )
                }
            }
        },
        actions = {
            when {
                userState is UserState.Logged ->
                    if ((userState as UserState.Logged).user.profilePicture?.isNotEmpty() == true &&
                        (userState as UserState.Logged).user.profilePicture !== null
                    ) {
                        val painter =
                            rememberAsyncImagePainter(
                                model = (userState as UserState.Logged).user.profilePicture,
                            )
                        IconButton(onClick = {
                            showUserMenu = !showUserMenu
                        }) {
                            Icon(
                                painter = painter,
                                contentDescription = context.getString(R.string.user_menu),
                            )
                            DropdownMenu(
                                expanded = showUserMenu,
                                onDismissRequest = {
                                    showUserMenu = false
                                }) {
                                DropdownMenuItem(
                                    text = { Text(context.getString(R.string.configuration)) },
                                    trailingIcon = {
                                        Icon(
                                            Icons.Default.Settings,
                                            contentDescription = context.getString(
                                                R.string.configuration
                                            ),
                                        )
                                    },
                                    onClick = {
                                        showUserMenu = false // Close the menu
                                        navController.navigate(Configuration) // Navigate to ConfigurationScreen
                                    },
                                )
                                DropdownMenuItem(
                                    text = { Text(context.getString(R.string.logout)) },
                                    trailingIcon = {
                                        Icon(
                                            Icons.AutoMirrored.Filled.ExitToApp,
                                            contentDescription = context.getString(
                                                R.string.logout
                                            ),
                                        )
                                    },
                                    onClick = {
                                        coroutineScope.launch {
                                            authService.signOut()
                                        }
                                    },
                                )
                            }
                        }
                    } else {
                        IconButton(onClick = {
                            showUserMenu = !showUserMenu
                        }) {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = context.getString(R.string.user_menu),
                            )
                            DropdownMenu(
                                expanded = showUserMenu,
                                onDismissRequest = {
                                    showUserMenu = false
                                }) {
                                DropdownMenuItem(
                                    text = { Text(context.getString(R.string.configuration)) },
                                    trailingIcon = {
                                        Icon(
                                            Icons.Default.Settings,
                                            contentDescription = context.getString(
                                                R.string.configuration
                                            ),
                                        )
                                    },
                                    onClick = {
                                        showUserMenu = false // Close the menu
                                        navController.navigate(Configuration) // Navigate to ConfigurationScreen
                                    },
                                )
                                DropdownMenuItem(
                                    text = { Text(context.getString(R.string.logout)) },
                                    trailingIcon = {
                                        Icon(
                                            Icons.AutoMirrored.Filled.ExitToApp,
                                            contentDescription = context.getString(
                                                R.string.logout
                                            ),
                                        )
                                    },
                                    onClick = {
                                        coroutineScope.launch {
                                            authService.signOut()
                                        }
                                    },
                                )
                            }
                        }
                    }

                else -> {
                }
            }
        },
    )
}