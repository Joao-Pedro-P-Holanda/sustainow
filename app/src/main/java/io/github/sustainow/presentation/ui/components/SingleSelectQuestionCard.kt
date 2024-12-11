package io.github.sustainow.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.sustainow.domain.model.Question
import io.github.sustainow.domain.model.QuestionAlternative
import io.github.sustainow.presentation.theme.AppTheme
import io.github.sustainow.presentation.theme.onSurfaceDarkHighContrast
import io.github.sustainow.presentation.theme.scrimLight
import kotlin.time.Duration

@Composable
fun SingleSelectQuestionCard(
    question: Question.SingleSelect,
    onAlternativeSelected: (QuestionAlternative) -> Unit,
) {
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
                containerColor = onSurfaceDarkHighContrast, // Cor do fundo do Card
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
        ) {
            Text(
                text = question.text,
                style = MaterialTheme.typography.titleMedium.copy(color = scrimLight),
                modifier = Modifier.padding(bottom = 8.dp),
            )

            HorizontalDivider(
                modifier = Modifier.padding(bottom = 10.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            )

            var selectedAlternativeText by rememberSaveable { mutableStateOf<String?>(null) }

            question.alternatives.forEach { alternative ->
                Row(
                    verticalAlignment = Alignment.CenterVertically, // Alinha os itens verticalmente
                    modifier =
                        Modifier
                            .padding(vertical = 6.dp) // Adiciona espaçamento entre os itens
                            .fillMaxWidth(),
                ) {
                    RadioButton(
                        selected = selectedAlternativeText == alternative.text,
                        onClick = {
                            selectedAlternativeText = alternative.text
                            onAlternativeSelected(alternative)
                        },
                    )
                    Text(
                        text = alternative.text,
                        style = MaterialTheme.typography.bodyMedium.copy(color = scrimLight),
                        modifier =
                            Modifier
                                .padding(start = 8.dp) // Espaçamento entre o botão e o texto
                                .weight(1f), // Faz o texto ocupar o espaço restante
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun SingleSelectQuestionCardPreview() {
    val question =
        Question.SingleSelect(
            name = "Number of plastic bags used per week",
            text = "How many plastic bags do you use in a week?",
            alternatives =
                listOf(
                    QuestionAlternative(id = 1, "carbon", text = "1", value = 20f, timePeriod = Duration.ZERO, unit = "bags"),
                    QuestionAlternative(id = 2, "carbon", text = "2", value = 40f, timePeriod = Duration.ZERO, unit = "bags"),
                    QuestionAlternative(id = 3, "carbon", text = "3", value = 60f, timePeriod = Duration.ZERO, unit = "bags"),
                    QuestionAlternative(id = 4, "carbon", text = "4", value = 80f, timePeriod = Duration.ZERO, unit = "bags"),
                ),
            dependencies = emptyList(),
        )
    AppTheme {
        SingleSelectQuestionCard(
            question = question,
            onAlternativeSelected = {
                Log.i(
                    "Single Select Question Preview",
                    "selected ${it.text}",
                )
            },
        )
    }
}
