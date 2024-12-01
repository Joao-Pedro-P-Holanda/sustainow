package io.github.sustainow.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.unit.dp
import io.github.sustainow.domain.model.Question
import io.github.sustainow.domain.model.QuestionAlternative
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import io.github.sustainow.presentation.theme.AppTheme
import io.github.sustainow.presentation.theme.onSurfaceDarkHighContrast
import io.github.sustainow.presentation.theme.scrimLight
import io.github.sustainow.presentation.theme.onBackgroundDarkHighContrast
import kotlin.time.Duration
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.runtime.saveable.Saver

@Composable
fun NumericalSelectQuestionCard(
    question: Question.Numerical,
    onAlternativeSelected: (QuestionAlternative) -> Unit,
) {
    // Defina um Saver personalizado para TextFieldValue
    val textFieldValueSaver = Saver<TextFieldValue, String>(
        save = { it.text }, // Salva apenas o texto do TextFieldValue
        restore = { TextFieldValue(it) } // Restaura o TextFieldValue a partir do texto salvo
    )

    var textFieldValue by rememberSaveable(stateSaver = textFieldValueSaver) {
        mutableStateOf(TextFieldValue())
    }

    Card(
        modifier = Modifier
            .width(360.dp)
            .padding(vertical = 10.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(0.dp),
            )
            .padding(horizontal = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = onSurfaceDarkHighContrast, // Cor do fundo do Card
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            // Cabeçalho com nome e texto da questão
            Text(
                text = question.name,
                style = MaterialTheme.typography.titleMedium.copy(color = scrimLight),
                modifier = Modifier.padding(bottom = 4.dp),
            )
            Text(
                text = question.text,
                style = MaterialTheme.typography.bodyMedium.copy(color = scrimLight),
                modifier = Modifier.padding(bottom = 8.dp),
            )

            HorizontalDivider(
                modifier = Modifier.padding(bottom = 10.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            )

            // Campo de texto numérico
            BasicTextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    textFieldValue = newValue
                    val alternative = question.alternatives.firstOrNull()
                    if (alternative != null) {
                        onAlternativeSelected(
                            alternative.copy(value = newValue.text.toFloatOrNull() ?: 0f)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .background(color = onBackgroundDarkHighContrast),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = scrimLight),
                singleLine = true,
            )
        }
    }
}





@Composable
@Preview
fun NumericalSelectQuestionCardPreview() {
    val question = Question.Numerical(
        area = "carbon",
        name = "Plastic Bags Usage",
        text = "How many plastic bags do you use in a week?",
        alternatives = listOf(
            QuestionAlternative(
                "carbon",
                text = "Number of bags",
                value = 0f,
                timePeriod = Duration.ZERO,
                unit = "bags"
            )
        ),
        dependencies = emptyList()
    )
    AppTheme {
        NumericalSelectQuestionCard(
            question = question,
            onAlternativeSelected = {
                Log.i("Numerical Question Preview", "Selected value: ${it.value}")
            }
        )
    }
}
