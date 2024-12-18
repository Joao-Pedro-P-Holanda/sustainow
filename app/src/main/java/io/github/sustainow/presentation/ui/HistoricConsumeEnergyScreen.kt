package io.github.sustainow.presentation.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.sustainow.domain.model.CardConsumeData
import io.github.sustainow.presentation.ui.components.HorizontalConsumeCard
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoricConsumeEnergyScreen(
    navController: NavController,
) {
    val scrollState = rememberScrollState()
    var sortType by remember { mutableStateOf(SortType.DATE_ASC) }
    var mockData by remember {
        mutableStateOf(
            listOf(
                CardConsumeData(realConsume = 120f, expectedConsume = 100f, unit = "kWh", mes = 1, date = LocalDate.of(2024, 1, 15)),
                CardConsumeData(realConsume = 110f, expectedConsume = 90f, unit = "kWh", mes = 2, date = LocalDate.of(2024, 2, 12)),
                CardConsumeData(realConsume = 100f, expectedConsume = 95f, unit = "kWh", mes = 3, date = LocalDate.of(2024, 3, 10)),
                CardConsumeData(realConsume = 130f, expectedConsume = 120f, unit = "kWh", mes = 4, date = LocalDate.of(2024, 4, 8)),
                CardConsumeData(realConsume = 140f, expectedConsume = 130f, unit = "kWh", mes = 5, date = LocalDate.of(2024, 5, 6)),
                CardConsumeData(realConsume = 150f, expectedConsume = 140f, unit = "kWh", mes = 6, date = LocalDate.of(2024, 6, 11)),
                CardConsumeData(realConsume = 160f, expectedConsume = 150f, unit = "kWh", mes = 7, date = LocalDate.of(2024, 7, 15)),
                CardConsumeData(realConsume = 170f, expectedConsume = 160f, unit = "kWh", mes = 8, date = LocalDate.of(2024, 8, 12)),
                CardConsumeData(realConsume = 180f, expectedConsume = 170f, unit = "kWh", mes = 9, date = LocalDate.of(2024, 9, 10)),
                CardConsumeData(realConsume = 190f, expectedConsume = 180f, unit = "kWh", mes = 10, date = LocalDate.of(2024, 10, 7)),
                CardConsumeData(realConsume = 200f, expectedConsume = 190f, unit = "kWh", mes = 11, date = LocalDate.of(2024, 11, 5)),
                CardConsumeData(realConsume = 210f, expectedConsume = 200f, unit = "kWh", mes = 12, date = LocalDate.of(2024, 12, 20))
            )
        )
    }

    LaunchedEffect(sortType) {
        mockData = when (sortType) {
            SortType.DATE_ASC -> mockData.sortedBy { it.date }
            SortType.DATE_DESC -> mockData.sortedByDescending { it.date }
            SortType.REAL_CONSUME_ASC -> mockData.sortedBy { it.realConsume }
            SortType.REAL_CONSUME_DESC -> mockData.sortedByDescending { it.realConsume }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Header
        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(color = MaterialTheme.colorScheme.outlineVariant)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Histórico de Consumo de Energia",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    fontSize = 28.sp,
                    lineHeight = 36.sp
                )
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // Content Container
        Column(
            modifier = Modifier
                .width(412.dp)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Ordering Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = { sortType = SortType.DATE_ASC }) {
                            Text(text = "Data Crescente")
                        }
                        Button(onClick = { sortType = SortType.DATE_DESC }) {
                            Text(text = "Data Decrescente")
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = { sortType = SortType.REAL_CONSUME_ASC }) {
                            Text(text = "Consumo Crescente")
                        }
                        Button(onClick = { sortType = SortType.REAL_CONSUME_DESC }) {
                            Text(text = "Consumo Decrescente")
                        }
                    }
                }
            }

            // Consumptions Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                )
                mockData.forEach { data ->
                    HorizontalConsumeCard(cardConsumeData = data)
                }
            }
        }
    }
}

enum class SortType {
    DATE_ASC, DATE_DESC, REAL_CONSUME_ASC, REAL_CONSUME_DESC
}
