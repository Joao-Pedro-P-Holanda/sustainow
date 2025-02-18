package io.github.sustainow.presentation.ui.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.sustainow.domain.model.RoutineTask
import io.github.sustainow.presentation.ui.getTaskColor
import io.github.sustainow.presentation.ui.utils.toLocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun TaskCard(
    task: RoutineTask,
    onCheck: (Boolean) -> Unit
) {
    if (!task.complete) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(getTaskColor(task))
                    .fillMaxWidth()
                    .height(8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.name,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = task.description ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (!task.complete) {
                    IconButton(
                        onClick = { onCheck(true) }, // Marca a tarefa como concluída
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = "Marcar como concluída",
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}

fun getTaskColor(task: RoutineTask): Color {
    return when (task.area.toString()) { // Alternando cores
        "energy" -> Color(0xFFDDE85C) // Verde claro
        "water" -> Color(0xFF7376E6) // Azul
        "carboon" -> Color(0xFFA56E3B) // Marrom
        else -> Color(0xFF58B070) // Verde escuro
    }
}
@Preview
@Composable
fun PreviewTaskCard(){
    TaskCard(
        task = RoutineTask(
            id = 1,
            metaDataId = 1,
            name = "Card Teste",
            description = "lorem ispum",
            complete = false,
            area = "energy",
            dueDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        ),
        onCheck = {}

        )
}