package io.github.sustainow.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.sustainow.domain.model.Question
import io.github.sustainow.domain.model.QuestionAlternative
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import io.github.sustainow.presentation.theme.*

@Composable
fun SingleSelectQuestionCard(
    question: Question.SingleSelect,
    onAlternativeSelected: (QuestionAlternative) -> Unit
) {

    Card(
        modifier = Modifier
            .width(360.dp)
            .padding(vertical = 10.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(0.dp)
            )
            .padding(horizontal = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = onSurfaceDarkHighContrast // Cor do fundo do Card
        )
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                text = question.text,
                style = MaterialTheme.typography.titleMedium.copy(color = scrimLight),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(bottom = 10.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )

            var selectedAlternativeText by rememberSaveable { mutableStateOf<String?>(null) }

            question.alternatives.forEach { alternative ->
                Row(
                    verticalAlignment = Alignment.CenterVertically, // Alinha os itens verticalmente
                    modifier = Modifier
                        .padding(vertical = 6.dp) // Adiciona espaçamento entre os itens
                        .fillMaxWidth()
                ) {
                    RadioButton(
                        selected = selectedAlternativeText == alternative.text,
                        onClick = {
                            selectedAlternativeText = alternative.text
                            onAlternativeSelected(alternative)
                        }
                    )
                    Text(
                        text = alternative.text,
                        style = MaterialTheme.typography.bodyMedium.copy(color = scrimLight),
                        modifier = Modifier
                            .padding(start = 8.dp) // Espaçamento entre o botão e o texto
                            .weight(1f) // Faz o texto ocupar o espaço restante
                    )
                }
            }
        }
    }
}

