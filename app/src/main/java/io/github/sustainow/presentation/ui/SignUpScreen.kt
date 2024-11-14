package io.github.sustainow.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import io.github.sustainow.R
import io.github.sustainow.presentation.viewmodel.SignUpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val logoResource = painterResource(id = R.drawable.sustainow_logo_transparent)

    val email by viewModel.email.collectAsState()
    val emailError by viewModel.emailError.collectAsState()

    val password by viewModel.password.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()

    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val confirmPasswordError by viewModel.confirmPasswordError.collectAsState()

    val firstName by viewModel.firstName.collectAsState()
    val firstNameError by viewModel.firstNameError.collectAsState()

    val lastName by viewModel.lastName.collectAsState()
    val lastNameError by viewModel.lastNameError.collectAsState()

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
                    .defaultMinSize(minHeight = 140.dp)
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
            Image(logoResource, contentDescription = null, modifier = modifier.requiredSize(200.dp, 200.dp))
        }

        Text(text = context.getString(R.string.signup_headline_message), style = MaterialTheme.typography.headlineMedium)

        Column(
            modifier = modifier.fillMaxSize().padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    label = {
                        Text(context.getString(R.string.signup_first_name_field_text))
                    },
                    value = firstName,
                    onValueChange = { viewModel.onFirstNameChange(context, it) },
                    modifier = modifier.weight(1f),
                    maxLines = 1,
                    isError = firstNameError != null,
                    supportingText = { Text(firstNameError?.message ?: "") },
                )

                TextField(
                    label = {
                        Text(context.getString(R.string.signup_last_name_field_text))
                    },
                    value = lastName,
                    onValueChange = { viewModel.onLastNameChange(context, it) },
                    modifier = modifier.weight(1f),
                    maxLines = 1,
                    isError = lastNameError != null,
                    supportingText = { Text(lastNameError?.message ?: "") },
                )
            }
            TextField(
                modifier = modifier.fillMaxWidth(),
                label = { Text(context.getString(R.string.signup_email_field_text)) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                value = email,
                onValueChange = { viewModel.onEmailChange(context, it) },
                maxLines = 1,
                isError = emailError != null,
                supportingText = { Text(emailError?.message ?: "") },
            )
            TextField(
                modifier = modifier.fillMaxWidth(),
                label = {
                    Text(context.getString(R.string.signup_password_field_text))
                },
                value = password,
                onValueChange = { viewModel.onPasswordChange(context, it) },
                visualTransformation = PasswordVisualTransformation(),
                maxLines = 1,
                isError = passwordError != null,
                supportingText = { Text(passwordError?.message ?: "") },
            )
            TextField(
                modifier = modifier.fillMaxWidth(),
                label = {
                    Text(context.getString(R.string.signup_confirm_password_field_text))
                },
                value = confirmPassword,
                onValueChange = {
                    viewModel.onConfirmPasswordChange(it)
                },
                visualTransformation = PasswordVisualTransformation(),
                maxLines = 1,
                isError = confirmPasswordError != null,
                supportingText = { Text(confirmPasswordError?.message ?: "") },
            )

            Button(onClick = {
                viewModel.signUpWithEmailAndPassword(context, email, password, confirmPassword, firstName, lastName)
            }, enabled = email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                Text(text = context.getString(R.string.signup_email_button_text))
            }
            Text(
                text = AnnotatedString(context.getString(R.string.signup_redirect_login_text)),
                modifier =
                    modifier.clickable {
                        viewModel.redirectLogin()
                    },
                style = MaterialTheme.typography.labelLarge.copy(textDecoration = TextDecoration.Underline),
            )
            when (unknownErrorState) {
                true ->
                    BasicAlertDialog(
                        onDismissRequest =
                            { viewModel.dismissUnknownErrorDialog() },
                    ) {
                        Text(context.getString(R.string.auth_unkown_error_message))
                    }
                else -> {
                }
            }
        }
    }
}
