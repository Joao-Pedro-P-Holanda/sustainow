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
import io.github.sustainow.domain.model.FormularyAnswer
import io.github.sustainow.domain.model.Question
import io.github.sustainow.presentation.theme.AppTheme
import kotlin.time.Duration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumericalSelectQuestionCard(
    question: Question.Numerical,
    onAnswerAdded: (FormularyAnswer) -> Unit,
    onAnswerRemoved: (FormularyAnswer) -> Unit,
    selectedAnswers: List<FormularyAnswer>
) {
    var textFieldValue by rememberSaveable { mutableStateOf(
        selectedAnswers.find { it.id == question.id }?.value?.toString() ?: ""
    ) }
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

                    if(newValue.isBlank()){

                        val answer = selectedAnswers.find { it.id == question.id }

                        if(answer != null){
                            onAnswerRemoved(answer)
                        }
                        isError = false
                        errorMessage = ""
                    }
                    else{
                        try {
                            val floatValue = newValue.toFloat()
                            isError = false
                            errorMessage = ""

                            val newFormAnswer = question.alternatives.firstOrNull()?.let {
                                FormularyAnswer(
                                    id = it.id,
                                    uid = it.uid,
                                    groupName = it.groupName,
                                    value = floatValue,
                                    unit = it.unit,
                                    month = it.month,
                                )
                            }
                            if (newFormAnswer != null) {
                                onAnswerAdded(newFormAnswer)
                            }
                        } catch (e: NumberFormatException) {
                            isError = true
                            errorMessage = context.getString(R.string.invalid_numerial_input_value)
                        }
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
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                textStyle = MaterialTheme.typography.bodyLarge,
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
    val formAnswers = listOf(
        FormularyAnswer(id = 1, uid = "", groupName = "test_group", value = 0f, unit = "kg", month = 12),
    )

    val question =
        Question.Numerical(
            name = "Plastic Bags Usage",
            text = "How many plastic bags do you use in a week?",
            alternatives =
                listOf(
                    FormularyAnswer(id = 1, uid = "", groupName = "test_group", value = 0f, unit = "kg", month = 12),
                    ),
            dependencies = emptyList(),
        )
    AppTheme {
        NumericalSelectQuestionCard(
            question = question,
            onAnswerAdded = {
                Log.i("Numerical Question Preview", "Selected value: ${it.value}")
            },
            onAnswerRemoved = {
                Log.i("Numerical Question Preview", "Unselected value: ${it.value}")
            },
            selectedAnswers = formAnswers
        )
    }
}
