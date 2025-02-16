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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.sustainow.domain.model.FormularyAnswerCreate
import io.github.sustainow.domain.model.Question
import io.github.sustainow.presentation.theme.AppTheme
import io.github.sustainow.presentation.theme.onSurfaceDarkHighContrast
import io.github.sustainow.presentation.theme.scrimLight

@Composable
fun MultiSelectQuestionCard(
    question: Question.MultiSelect,
    onAnswerAdded: (FormularyAnswerCreate) -> Unit,
    onAnswerRemoved: (FormularyAnswerCreate) -> Unit,
    selectedAnswers: List<FormularyAnswerCreate>,
) {
    Card(
        modifier =
            Modifier
                .width(360.dp)
                .padding(vertical = 10.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(0.dp),
                ).padding(horizontal = 10.dp),
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

                val selected = selectedAnswers.any { it.copy(text = alternative.text) == alternative }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier
                            .padding(vertical = 6.dp)
                            .fillMaxWidth(),
                ) {
                    Checkbox(
                        checked = selected,
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                onAnswerAdded(alternative)
                            } else {
                                onAnswerRemoved(alternative)
                            }
                        },
                    )

                    alternative.text?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium.copy(color = scrimLight),
                            modifier =
                                Modifier
                                    .padding(start = 8.dp)
                                    .weight(1f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun MultiSelectQuestionCardPreview() {
    val formAnswers =
        listOf(
            FormularyAnswerCreate(uid = "", groupName = "test_group", value = 1f, unit = "transport", formId = 1, questionId = 1),
            FormularyAnswerCreate(uid = "", groupName = "test_group", value = 2f, unit = "transport", formId = 1, questionId = 1),
            FormularyAnswerCreate(uid = "", groupName = "test_group", value = 3f, unit = "transport", formId = 1, questionId = 1),
            FormularyAnswerCreate(uid = "", groupName = "test_group", value = 3f, unit = "transport", formId = 1, questionId = 1),
        )

    val question =
        Question.MultiSelect(
            id = 1,
            name = "transport",
            text = "What transport do you use most?:",
            alternatives = formAnswers,
            dependencies = emptyList(),
        )
    AppTheme {
        MultiSelectQuestionCard(
            question = question,
            onAnswerAdded = {
                Log.i("Multi Select Question Preview", "selected ${it.value}")
            },
            onAnswerRemoved = {
                Log.i("Multi Select Question Preview", "unselected ${it.value}")
            },
            selectedAnswers = formAnswers,
        )
    }
}
