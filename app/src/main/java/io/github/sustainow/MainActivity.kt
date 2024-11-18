package io.github.sustainow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import io.github.sustainow.presentation.theme.AppTheme
import io.github.sustainow.presentation.ui.HomeScreen
import io.github.sustainow.presentation.ui.LoginScreen
import io.github.sustainow.presentation.ui.SignUpScreen
import io.github.sustainow.presentation.viewmodel.HomeViewModel
import io.github.sustainow.presentation.viewmodel.LoginViewModel
import io.github.sustainow.presentation.viewmodel.SignUpViewModel
import io.github.sustainow.service.auth.AuthService
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable object Home

@Serializable object Authentication

@Serializable object Login

@Serializable object SignUp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()

                val userState by authService.user.collectAsState()

                Scaffold(modifier = Modifier.safeDrawingPadding()) { innerPadding ->
                    NavHost(navController = navController, startDestination = Home, modifier = Modifier.padding(innerPadding)) {
                        composable<Home> {
                            val homeViewModel: HomeViewModel by viewModels()
                            HomeScreen(viewModel = homeViewModel, userState = userState, redirectLogin = {
                                navController.navigate(Login) {
                                }
                            })
                        }
                        navigation<Authentication>(startDestination = SignUp) {
                            composable<Login> {
                                val loginViewModel: LoginViewModel by viewModels(
                                    extrasProducer = {
                                        defaultViewModelCreationExtras.withCreationCallback<LoginViewModel.Factory> { factory ->
                                            factory.create(
                                                navigateSignUp = {
                                                    navController.navigate(SignUp) {
                                                        popUpTo(navController.graph.findStartDestination().id) {
                                                            saveState = true
                                                        }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                    }
                                                },
                                                navigateSuccess = {
                                                    navController.navigate(Home) {
                                                        popUpTo(navController.graph.findStartDestination().id) {
                                                            saveState = true
                                                        }
                                                        launchSingleTop = true
                                                    }
                                                },
                                            )
                                        }
                                    },
                                )
                                LoginScreen(loginViewModel)
                            }
                            composable<SignUp> {
                                val signUpViewModel: SignUpViewModel by viewModels(
                                    extrasProducer = {
                                        defaultViewModelCreationExtras.withCreationCallback<SignUpViewModel.Factory> { factory ->
                                            factory.create(
                                                navigateLogin = {
                                                    navController.navigate(Login) {
                                                        popUpTo(navController.graph.findStartDestination().id) {
                                                            saveState = true
                                                        }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                    }
                                                },
                                                navigateSuccess = {
                                                    navController.navigate(Home) {
                                                        popUpTo(navController.graph.findStartDestination().id) {
                                                            saveState = true
                                                        }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                    }
                                                },
                                            )
                                        }
                                    },
                                )
                                SignUpScreen(signUpViewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
