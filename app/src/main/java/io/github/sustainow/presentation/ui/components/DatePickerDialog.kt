package io.github.sustainow.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate

@Composable
fun DatePickerDialog(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val year = initialDate.year
    val month = initialDate.monthValue - 1 // Base zero
    val day = initialDate.dayOfMonth

    val context = LocalContext.current
    val datePicker = android.app.DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            onDateSelected(LocalDate.of(selectedYear, selectedMonth + 1, selectedDay))
        },
        year,
        month,
        day
    )

    datePicker.setOnDismissListener { onDismiss() }
    datePicker.show()
}
