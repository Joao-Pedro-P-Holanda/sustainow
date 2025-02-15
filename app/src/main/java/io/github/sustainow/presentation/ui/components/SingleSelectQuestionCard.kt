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
import io.github.sustainow.domain.model.FormularyAnswer
import io.github.sustainow.domain.model.Question
import io.github.sustainow.presentation.theme.AppTheme
import io.github.sustainow.presentation.theme.onSurfaceDarkHighContrast
import io.github.sustainow.presentation.theme.scrimLight
import kotlin.time.Duration

@Composable
fun SingleSelectQuestionCard(
    question: Question.SingleSelect,
    onAnswerAdded: (FormularyAnswer) -> Unit,
    onAnswerRemoved: (FormularyAnswer) -> Unit,
    selectedAnswers: List<FormularyAnswer>
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

            question.alternatives.forEach { alternative ->
                Log.i("alternative", "$alternative")
                Log.i("list", "$selectedAnswers")
                val selected = selectedAnswers.any { it == alternative }

                Row(
                    verticalAlignment = Alignment.CenterVertically, // Alinha os itens verticalmente
                    modifier =
                        Modifier
                            .padding(vertical = 6.dp) // Adiciona espaçamento entre os itens
                            .fillMaxWidth(),
                ) {
                    RadioButton(
                        selected = selected,
                        onClick = {
                            if (selected){
                                onAnswerRemoved(alternative)
                            }
                            else{
                                onAnswerAdded(alternative)
                            }
                        },
                    )
                    alternative.text?.let {
                        Text(
                            text = it,
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
}

@Composable
@Preview
fun SingleSelectQuestionCardPreview() {
    val formAnswers = listOf(
        FormularyAnswer(id = 1, uid = "", groupName = "test_group", value = 2f, unit = "kg", ),
        FormularyAnswer(id = 1, uid = "", groupName = "test_group", value = 3f, unit = "kg", ),
        FormularyAnswer(id = 1, uid = "", groupName = "test_group", value = 4f, unit = "kg", ),
    )

    val question = Question.SingleSelect(
            name = "Number of plastic bags used per week",
            text = "How many plastic bags do you use in a week?",
            alternatives = formAnswers,
            dependencies = emptyList(),
        )
    AppTheme {
        SingleSelectQuestionCard(
            question = question,
            onAnswerAdded = {
                Log.i(
                    "Single Select Question Preview",
                    "selected ${it.value}",
                )
            },
            onAnswerRemoved = {
                Log.i(
                    "Single Select Question Preview",
                    "removed ${it.value}",
                )
            },
            selectedAnswers = formAnswers
        )
    }
}
