package io.github.sustainow.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import io.github.sustainow.R
import io.github.sustainow.presentation.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val logoResource = painterResource(id = R.drawable.sustainow_logo_transparent)

    val email by viewModel.email.collectAsState()
    val emailError by viewModel.emailError.collectAsState()

    val password by viewModel.password.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()

    val unknownErrorState by viewModel.unknownErrorState.collectAsState()

    Column(
        modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(48.dp),
    ) {
        Box(
            modifier =
                modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 280.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape =
                            RoundedCornerShape(
                                bottomStart = 120.dp,
                                bottomEnd = 120.dp,
                            ), // only bottom borders rounded
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                logoResource,
                contentDescription = null,
                modifier = modifier.requiredSize(200.dp, 200.dp),
            )
        }
        Text(
            text = context.getString(R.string.login_welcome_message),
            style = MaterialTheme.typography.headlineMedium,
        )

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                label = { Text(context.getString(R.string.login_email_field_label)) },
                value = email,
                onValueChange = { viewModel.onEmailChange(context, it) },
                isError = emailError != null,
                supportingText = { Text(emailError?.message ?: "") },
            )
            TextField(
                label = { Text(context.getString(R.string.login_password_field_label)) },
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) },
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError != null,
                supportingText = { Text(passwordError?.message ?: "") },
            )
            Text(
                text = AnnotatedString(context.getString(R.string.login_forgot_password_text)),
                modifier =
                    modifier.clickable {
                    },
                style = MaterialTheme.typography.labelMedium.copy(textDecoration = TextDecoration.Underline),
            )
            Button(onClick = {
                viewModel.signInWithEmailAndPassword(context, email, password)
            }, enabled = email.isNotEmpty() && password.isNotEmpty()) {
                Text(text = context.getString(R.string.login_email_button_text))
            }
            Text(
                text = AnnotatedString(context.getString(R.string.login_redirect_signup_text)),
                modifier =
                    modifier.clickable {
                        viewModel.redirectSignUp()
                    },
                style = MaterialTheme.typography.labelLarge.copy(textDecoration = TextDecoration.Underline),
            )
            when (unknownErrorState) {
                true ->
                    BasicAlertDialog(
                        onDismissRequest =
                            { viewModel.dismissUnknownErrorDialog() },
                    ) {
                        Surface(
                            modifier = Modifier.wrapContentWidth().wrapContentHeight(),
                            shape = MaterialTheme.shapes.large,
                            tonalElevation = AlertDialogDefaults.TonalElevation,
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(context.getString(R.string.auth_unkown_error_message))
                            }
                        }
                    }
                else -> {
                }
            }
        }
    }
}
