package io.github.sustainow

import Authentication
import CollectiveActions
import Configuration
import Consume
import ConsumptionMainPage
import CreateCollectiveAction
import ExpectedCarbonFootprint
import ExpectedEnergyConsumption
import ExpectedWaterConsumption
import Home
import Login
import RealEnergyConsumption
import RealWaterConsumption
import Routines
import SearchCollectiveActions
import SignUp
import UpdateCollectiveAction
import ViewCollectiveAction
import ViewRoutine
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import io.github.sustainow.presentation.theme.AppTheme
import io.github.sustainow.presentation.ui.ConfigurationScreen
import io.github.sustainow.presentation.ui.actions.CollectiveActionScreen
import io.github.sustainow.presentation.ui.ConsumptionMainScreen
import io.github.sustainow.presentation.ui.ExpectedCarbonFootprintScreen
import io.github.sustainow.presentation.ui.ExpectedEnergyScreen
import io.github.sustainow.presentation.ui.HistoricCarbonFootprintScreen
import io.github.sustainow.presentation.ui.HistoricConsumeEnergyScreen
import io.github.sustainow.presentation.ui.HistoricConsumeWaterScreen
import io.github.sustainow.presentation.ui.HistoricMainScreen
import io.github.sustainow.presentation.ui.HomeScreen
import io.github.sustainow.presentation.ui.LoginScreen
import io.github.sustainow.presentation.ui.RealEnergyConsumptionScreen
import io.github.sustainow.presentation.ui.actions.SearchCollectiveActionsScreen
import io.github.sustainow.presentation.ui.SignUpScreen
import io.github.sustainow.presentation.ui.actions.FormCollectiveActionScreen
import io.github.sustainow.presentation.ui.actions.SubmitAction
import io.github.sustainow.presentation.ui.components.BottomBar
import io.github.sustainow.presentation.ui.components.TopBar
import io.github.sustainow.presentation.viewmodel.CollectiveActionViewModel
import io.github.sustainow.presentation.viewmodel.FormularyViewModel
import io.github.sustainow.presentation.viewmodel.HomeViewModel
import io.github.sustainow.presentation.viewmodel.LoginViewModel
import io.github.sustainow.presentation.viewmodel.SearchCollectiveActionsViewModel
import io.github.sustainow.presentation.viewmodel.SignUpViewModel
import io.github.sustainow.routes.Historic
import io.github.sustainow.routes.HistoricCarbonFootprint
import io.github.sustainow.routes.HistoricConsumeEnergy
import io.github.sustainow.routes.HistoricConsumeWater
import io.github.sustainow.routes.HistoricMainPage
import io.github.sustainow.service.auth.AuthService
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.navigation.animation.AnimatedNavHost
import android.provider.Settings
import io.github.sustainow.presentation.ui.utils.scheduleNotification
import io.github.sustainow.presentation.viewmodel.HistoricViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authService: AuthService

    @OptIn(ExperimentalAnimationApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        scheduleNotification(this)
        setContent {
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val notificationManager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    if (!notificationManager.areNotificationsEnabled()) {
                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        }
                        context.startActivity(intent)
                    }
                }
            }

            var isDarkTheme by remember { mutableStateOf(false) }

            AppTheme(
                darkTheme = isDarkTheme
            ) {
                val navController = rememberNavController()

                val userState by authService.user.collectAsState()

                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentScreen =
                    backStackEntry?.destination?.let {
                        when (it.route) {
                            Login::class.qualifiedName -> Login
                            SignUp::class.qualifiedName -> SignUp
                            else -> Home
                        }
                    } ?: Home

                val previousBackStackEntry = navController.previousBackStackEntry
                val previousScreen =
                    previousBackStackEntry?.destination?.let {
                        when (it.route) {
                            Login::class.qualifiedName -> Login
                            SignUp::class.qualifiedName -> SignUp
                            else -> Home
                        }
                    } ?: Home

                // Verifica se há uma tela anterior e se a rota atual não é Login nem SignUp
                val canNavigateBack =
                    previousBackStackEntry != null &&
                            previousScreen != Login &&
                            previousScreen != SignUp

                Scaffold(
                    topBar = {
                        if (currentScreen != Login && currentScreen != SignUp) {
                            TopBar(navController, authService, userState, canNavigateBack)
                        }
                    },
                    modifier = Modifier.safeDrawingPadding(),
                    bottomBar = {
                        if (currentScreen != Login && currentScreen != SignUp) {
                            BottomBar(navController)
                        }
                    },
                ) { innerPadding ->
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = "Home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<Home>(
                            enterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { it } },
                            exitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { -it } },
                            popEnterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { -it } },
                            popExitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { it } }
                        ) {
                            val homeViewModel: HomeViewModel by viewModels()
                            HomeScreen(
                                viewModel = homeViewModel,
                                navController = navController,
                                userState = userState,
                                redirectLogin = {
                                    navController.navigate(Login) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            inclusive = true
                                        }
                                    }
                                })
                        }
                        navigation<Consume>(startDestination = ConsumptionMainPage) {
                            composable<ConsumptionMainPage>(
                                enterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { it } },
                                exitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { -it } },
                                popEnterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { -it } },
                                popExitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { it } }
                            ) {
                                ConsumptionMainScreen(navController = navController)
                            }
                            // TODO remove placeholder when creating each new screen
                            composable<ExpectedEnergyConsumption>(
                                enterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { it } },
                                exitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { -it } },
                                popEnterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { -it } },
                                popExitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { it } }
                            ) {
                                val formularyViewModel =
                                    hiltViewModel<FormularyViewModel, FormularyViewModel.Factory>(
                                        creationCallback = { factory ->
                                            factory.create(
                                                area = "energy_consumption",
                                                type = "expected",
                                            )
                                        })
                                ExpectedEnergyScreen(navController, formularyViewModel)
                            }
                            composable<ExpectedWaterConsumption>(
                                enterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { it } },
                                exitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { -it } },
                                popEnterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { -it } },
                                popExitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { it } }
                            ) { Text(text = "Consumo de água") }
                            composable<ExpectedCarbonFootprint>(
                                enterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { it } },
                                exitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { -it } },
                                popEnterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { -it } },
                                popExitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { it } }
                            ) {
                                val formularyViewModel =
                                    hiltViewModel<FormularyViewModel, FormularyViewModel.Factory>(
                                        creationCallback = { factory ->
                                            factory.create(
                                                area = "carbon_footprint",
                                                type = "expected",
                                            )
                                        })
                                ExpectedCarbonFootprintScreen(navController, formularyViewModel)
                            }
                            composable<RealEnergyConsumption>(
                                enterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { it } },
                                exitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { -it } },
                                popEnterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { -it } },
                                popExitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { it } }
                            ) {
                                val viewModel =
                                    hiltViewModel<FormularyViewModel, FormularyViewModel.Factory>(
                                        creationCallback = { factory ->
                                            factory.create(
                                                area = "energy_consumption",
                                                type = "real",
                                            )
                                        })

                                RealEnergyConsumptionScreen(
                                    defaultErrorAction = { navController.popBackStack() },
                                    viewModel = viewModel,
                                )
                            }
                            composable<RealWaterConsumption> { Text(text = "Consumo de água real") }
                        }
                        navigation<Historic>(startDestination = HistoricMainPage) {
                            composable<HistoricMainPage>(
                                enterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { it } },
                                exitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { -it } },
                                popEnterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { -it } },
                                popExitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { it } }
                            ) {
                                HistoricMainScreen(navController = navController)
                            }
                            composable<HistoricConsumeWater>(
                                enterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { it } },
                                exitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { -it } },
                                popEnterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { -it } },
                                popExitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { it } }
                            ) {
                                val viewModel =
                                    hiltViewModel<HistoricViewModel, HistoricViewModel.Factory>(
                                        creationCallback = { factory ->
                                            factory.create(
                                                area = "water_consumption",
                                            )
                                        })
                                HistoricConsumeWaterScreen(navController, viewModel)

                            }
                            composable<HistoricConsumeEnergy>(
                                enterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { it } },
                                exitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { -it } },
                                popEnterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { -it } },
                                popExitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { it } }
                            ) {
                                val viewModel =
                                    hiltViewModel<HistoricViewModel, HistoricViewModel.Factory>(
                                        creationCallback = { factory ->
                                            factory.create(
                                                area = "energy_consumption",
                                            )
                                        })
                                HistoricConsumeEnergyScreen(navController, viewModel)

                            }
                            composable<HistoricCarbonFootprint>(
                                enterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { it } },
                                exitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { -it } },
                                popEnterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { -it } },
                                popExitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { it } }
                            ) {
                                val viewModel =
                                    hiltViewModel<HistoricViewModel, HistoricViewModel.Factory>(
                                        creationCallback = { factory ->
                                            factory.create(
                                                area = "carbon_footprint",
                                            )
                                        })
                                HistoricCarbonFootprintScreen(navController = navController, viewModel = viewModel)

                            }
                        }

                        navigation<CollectiveActions>(startDestination = SearchCollectiveActions) {
                            composable<SearchCollectiveActions>(
                                enterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { it } },
                                exitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { -it } },
                                popEnterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { -it } },
                                popExitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { it } }
                            ) {
                                val viewModel = hiltViewModel<SearchCollectiveActionsViewModel>()
                                SearchCollectiveActionsScreen(navController,viewModel)
                            }
                            composable<ViewCollectiveAction>(
                                enterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { it } },
                                exitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { -it } },
                                popEnterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { -it } },
                                popExitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { it } }
                            ) { backStackEntry ->
                                val viewObject:ViewCollectiveAction = backStackEntry.toRoute()
                                val viewModel = hiltViewModel<CollectiveActionViewModel,CollectiveActionViewModel.Factory>(creationCallback =
                                {
                                        factory ->
                                    factory.create(
                                        id = viewObject.id,
                                        successCreateCallback = null,
                                        successUpdateCallback = null,
                                        deleteCallback = {
                                            navController.popBackStack()
                                        }
                                    )
                                }
                                )
                                CollectiveActionScreen(userState,viewModel,
                                    navigateEditCallback = {navController.navigate(UpdateCollectiveAction(viewObject.id))},
                                    returnCallback={navController.popBackStack()})
                            }
                            composable<CreateCollectiveAction>(
                                enterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { it } },
                                exitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { -it } },
                                popEnterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { -it } },
                                popExitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { it } }
                            ) {
                                val viewModel = hiltViewModel<CollectiveActionViewModel,CollectiveActionViewModel.Factory>(creationCallback =
                                {
                                        factory ->
                                    factory.create(
                                        id = null,
                                        successCreateCallback = {navController.navigate(CollectiveActions){
                                            popUpTo(CreateCollectiveAction){
                                                inclusive=true
                                            }
                                        } },
                                        successUpdateCallback = null,
                                        deleteCallback = {
                                        }
                                    )
                                }
                                )
                                FormCollectiveActionScreen(viewModel,SubmitAction.CREATE)
                            }
                            composable<UpdateCollectiveAction>(
                                enterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { it } },
                                exitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { -it } },
                                popEnterTransition = { fadeIn(animationSpec = tween(700)) + slideInHorizontally { -it } },
                                popExitTransition = { fadeOut(animationSpec = tween(700)) + slideOutHorizontally { it } }
                            ){ backStackEntry->
                                val updateObject: UpdateCollectiveAction = backStackEntry.toRoute()
                                val viewModel = hiltViewModel<CollectiveActionViewModel,CollectiveActionViewModel.Factory>(creationCallback =
                                {
                                        factory ->
                                    factory.create(
                                        id = updateObject.id,
                                        successCreateCallback = null,
                                        successUpdateCallback = {navController.navigate(ViewCollectiveAction(updateObject.id)){
                                            popUpTo(UpdateCollectiveAction(updateObject.id)){
                                                inclusive=true
                                            }
                                        } },
                                        deleteCallback = {
                                        }
                                    )
                                }
                                )
                                FormCollectiveActionScreen(viewModel,SubmitAction.UPDATE)
                            }
                        }
                        navigation<Routines>(startDestination = ViewRoutine) {
                            composable<ViewRoutine> { }
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
                        composable<Configuration> {
                            ConfigurationScreen(
                                navController = navController,
                                userState = userState,
                                authService = authService,
                                onChangeTheme = {isDarkTheme = it}
                            )
                        }
                    }
                }
            }
        }
    }
}