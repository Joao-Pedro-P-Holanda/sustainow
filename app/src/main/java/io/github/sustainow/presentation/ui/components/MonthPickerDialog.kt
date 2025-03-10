package io.github.sustainow.presentation.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.time.YearMonth
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier

@Composable
fun MonthPickerDialog(
    initialMonth: YearMonth,
    onMonthSelected: (YearMonth) -> Unit,
    onDismiss: () -> Unit
) {
    val months = (1..12).toList()
    val years = (2000..2030).toList()

    var selectedMonth by remember { mutableStateOf(initialMonth.monthValue) }
    var selectedYear by remember { mutableStateOf(initialMonth.year) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onMonthSelected(YearMonth.of(selectedYear, selectedMonth)) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text("Selecionar Mês e Ano") },
        text = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                DropdownMenuBox(label = "Mês", items = months, selected = selectedMonth, onItemSelected = { selectedMonth = it })
                DropdownMenuBox(label = "Ano", items = years, selected = selectedYear, onItemSelected = { selectedYear = it })
            }
        }
    )
}