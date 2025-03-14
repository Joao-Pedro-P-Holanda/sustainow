package io.github.sustainow.presentation.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.sustainow.R
import io.github.sustainow.domain.model.CardConsumeData
import io.github.sustainow.repository.model.CardExpectedData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HorizontalEstimateCarbonFootprint(
    cardConsumeData: CardExpectedData,
    onCardClick: () -> Unit
) {
    val monthName = getMonthName(cardConsumeData.mes)
    val formattedDate = cardConsumeData.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { onCardClick() }, // Make the card clickable
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Coluna para o Mês e Data
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Título do Mês
                Text(
                    text = monthName,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.scrim,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 24.sp,
                        letterSpacing = 0.15.sp
                    )
                )

                // Data
                Text(
                    text = formattedDate,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.scrim,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 20.sp,
                        letterSpacing = 0.25.sp
                    )
                )
            }

            // Coluna para os Valores
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.Start
            ) {

                // Valor Esperado
                Text(
                    text = "Valor esperado: ${cardConsumeData.expectedFootprint} ${cardConsumeData.unit}",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.scrim,
                        textAlign = TextAlign.Center,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 16.sp,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewHorizontalEstimateCarbonFootprint() {
    HorizontalEstimateCarbonFootprint(
        cardConsumeData = CardExpectedData(
            expectedFootprint = 100f,
            unit = "kg",
            mes = 1,
            date = "01/2023"
        ),
        onCardClick = {}
    )
}
