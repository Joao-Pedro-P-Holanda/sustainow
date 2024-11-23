package io.github.sustainow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Serializable object Home

@Serializable object Authentication

@Serializable object Login

@Serializable object SignUp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var authService: AuthService

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()

                val userState by authService.user.collectAsState()

                val coroutineScope = rememberCoroutineScope() // Cria um escopo de coroutine

                Scaffold(
                    topBar = {
                        val currentDestination = navController.currentBackStackEntry?.destination?.route
                        if (currentDestination != "Login" && currentDestination != "SignUp") {
                            TopAppBar(
                                title = { Text(text = "Sustainow", color = MaterialTheme.colorScheme.onPrimaryContainer) },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                ),
                                navigationIcon = {
                                    // Gerenciar o estado do DropdownMenu
                                    val expanded = remember { mutableStateOf(false) }
                                    IconButton(onClick = {
                                        // Navegar para a tela de Login ao clicar no botão de voltar
                                        navController.navigate("Login") {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                inclusive = true // Remove todas as telas anteriores da pilha
                                            }
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Voltar"
                                        )
                                    }
                                    val logoResource = painterResource(id = R.drawable.sustainow_logo_transparent)
                                    // Menu suspenso com a opção de logout
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Spacer(modifier = Modifier.weight(1f)) // Adiciona espaço entre o botão de voltar e a logo
                                        // Tornar a logo clicável
                                        IconButton(onClick = { expanded.value = !expanded.value }) {
                                            Image(
                                                logoResource,
                                                contentDescription = null,
                                                modifier = Modifier.fillMaxWidth() // Ajusta a largura da imagem para preencher o espaço disponível
                                            )
                                        }
                                        Spacer(modifier = Modifier.weight(1f)) // Centraliza a logo
                                    }

                                    DropdownMenu(
                                        expanded = expanded.value,
                                        onDismissRequest = { expanded.value = false },
                                    ) {
                                        DropdownMenuItem(
                                            onClick = {
                                                expanded.value = false
                                                coroutineScope.launch {
                                                    authService.signOut() // Realiza o logout
                                                    navController.navigate("Login") { // Navega para a tela de login
                                                        popUpTo(navController.graph.findStartDestination().id) {
                                                            inclusive = true // Remove a pilha de navegação
                                                        }
                                                        launchSingleTop = true
                                                    }
                                                }
                                            },
                                            text = { Text("Logout") }
                                        )
                                    }
                                }
                            )
                        }
                    },
                    modifier = Modifier.safeDrawingPadding()
                ) { innerPadding ->
                    NavHost(navController = navController, startDestination = Home, modifier = Modifier.padding(innerPadding)) {
                        composable<Home> {
                            val homeViewModel: HomeViewModel by viewModels()
                            HomeScreen(viewModel = homeViewModel, userState = userState, redirectLogin = {
                                navController.navigate(Login) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        inclusive = true
                                    }
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
