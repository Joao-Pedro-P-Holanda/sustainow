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
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.sustainow.R

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val logoResource = painterResource(id = R.drawable.sustainow_logo_transparent)

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
        Text(text = context.getString(R.string.login_welcome_message), style = MaterialTheme.typography.headlineMedium)

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                label = { Text(context.getString(R.string.login_email_field_label)) },
                value = "",
                onValueChange = {},
            )
            TextField(label = {
                Text(context.getString(R.string.login_password_field_label))
            }, value = "", onValueChange = {}, visualTransformation = PasswordVisualTransformation())
            Text(
                text = AnnotatedString(context.getString(R.string.login_forgot_password_text)),
                modifier =
                    modifier.clickable {
                    },
                style = MaterialTheme.typography.labelMedium.copy(textDecoration = TextDecoration.Underline),
            )
            Button(onClick = { }) {
                Text(text = context.getString(R.string.login_email_button_text))
            }
            Text(
                text = AnnotatedString(context.getString(R.string.login_redirect_signup_text)),
                modifier =
                    modifier.clickable {
                    },
                style = MaterialTheme.typography.labelLarge.copy(textDecoration = TextDecoration.Underline),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLogin()  {
    LoginScreen()
}
