package io.github.sustainow.presentation.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.presentation.ui.components.LoadingModal
import io.github.sustainow.presentation.viewmodel.HomeViewModel

/*
* HomeScreen is the main screen of the application.
* TODO: remove logout button when the scope is finished
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    userState: UserState,
    redirectLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Log.i("HomeScreen", "UserState: $userState")
    when (userState) {
        is UserState.NotLogged -> {
            redirectLogin()
        }
        is UserState.Loading -> {
            LoadingModal()
        }
        is UserState.Logged -> {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Button(onClick = { viewModel.signOut() }) {
                    Text(text = "Logout")
                }
            }
        }
    }
}
