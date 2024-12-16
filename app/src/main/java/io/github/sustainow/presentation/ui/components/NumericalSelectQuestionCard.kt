package io.github.sustainow.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.sustainow.R
import io.github.sustainow.domain.model.Question
import io.github.sustainow.domain.model.QuestionAlternative
import io.github.sustainow.presentation.theme.AppTheme
import kotlin.time.Duration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumericalSelectQuestionCard(
    question: Question.Numerical,
    onAlternativeSelected: (QuestionAlternative) -> Unit,
) {
    var textFieldValue by rememberSaveable { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Card(
        modifier =
            Modifier
                .width(360.dp)
                .padding(vertical = 10.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(0.dp),
                )
                .padding(horizontal = 10.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
        ) {
            // Cabeçalho com nome e texto da questão
            question.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }
            Text(
                text = question.text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            HorizontalDivider(
                modifier = Modifier.padding(bottom = 10.dp),
                thickness = 1.dp,
            )

            // Campo de texto numérico
            TextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    textFieldValue = newValue
                    try {
                        val floatValue = newValue.toFloat()
                        isError = false
                        errorMessage = ""
                        val alternative = question.alternatives.firstOrNull()
                        if (alternative != null) {
                            onAlternativeSelected(
                                alternative.copy(value = floatValue),
                            )
                        }
                    } catch (e: NumberFormatException) {
                        isError = true
                        errorMessage = context.getString(R.string.invalid_numerial_input_value)
                    }
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                colors =
                    TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    ),
                textStyle = MaterialTheme.typography.bodyLarge.copy(MaterialTheme.colorScheme.scrim),
                singleLine = true,
                isError = isError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = {
                    if (isError) {
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error),
                        )
                    }
                },
            )
        }
    }
}

@Composable
@Preview
fun NumericalSelectQuestionCardPreview() {
    val question =
        Question.Numerical(
            name = "Plastic Bags Usage",
            text = "How many plastic bags do you use in a week?",
            alternatives =
                listOf(
                    QuestionAlternative(
                        id = 1,
                        "carbon",
                        text = "Number of bags",
                        value = 0f,
                        timePeriod = Duration.ZERO,
                        unit = "bags",
                    ),
                ),
            dependencies = emptyList(),
        )
    AppTheme {
        NumericalSelectQuestionCard(
            question = question,
            onAlternativeSelected = {
                Log.i("Numerical Question Preview", "Selected value: ${it.value}")
            },
        )
    }
}
