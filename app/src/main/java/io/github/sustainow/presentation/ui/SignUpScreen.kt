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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
fun SignUpScreen(modifier: Modifier = Modifier) {
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
                TextField(label = {
                    Text(context.getString(R.string.signup_first_name_field_text))
                }, value = "", onValueChange = {}, modifier = modifier.weight(1f))
                TextField(label = {
                    Text(context.getString(R.string.signup_last_name_field_text))
                }, value = "", onValueChange = {}, modifier = modifier.weight(1f))
            }
            TextField(
                modifier = modifier.fillMaxWidth(),
                label = { Text(context.getString(R.string.signup_email_field_text)) },
                value = "",
                onValueChange = {},
            )
            TextField(
                modifier = modifier.fillMaxWidth(),
                label = {
                    Text(context.getString(R.string.signup_password_field_text))
                },
                value = "",
                onValueChange = {},
                visualTransformation = PasswordVisualTransformation(),
            )
            TextField(modifier = modifier.fillMaxWidth(), label = {
                Text(context.getString(R.string.signup_confirm_password_field_text))
            }, value = "", onValueChange = {}, visualTransformation = PasswordVisualTransformation())

            Button(onClick = { }) {
                Text(text = context.getString(R.string.signup_email_button_text))
            }
            Text(
                text = AnnotatedString(context.getString(R.string.signup_redirect_login_text)),
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
fun PreviewSignupScreen() {
    SignUpScreen()
}
