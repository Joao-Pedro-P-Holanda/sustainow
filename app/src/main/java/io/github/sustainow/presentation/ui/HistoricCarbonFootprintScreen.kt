package io.github.sustainow.presentation.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import io.github.sustainow.presentation.ui.utils.LineChartConsumption
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoricCarbonFootprintScreen(
    navController: NavController,
) {
    val scrollState = rememberScrollState()
    var sortType by remember { mutableStateOf(SortType.DATE_ASC) }

    var switch by remember { mutableStateOf(false) }

    var mockData by remember {
        mutableStateOf(
            listOf(
                CardConsumeData(realConsume = 5f, expectedConsume = 4.5f, unit = "kg", mes = 1, date = LocalDate.of(2024, 1, 15)),
                CardConsumeData(realConsume = 7f, expectedConsume = 6.5f, unit = "kg", mes = 2, date = LocalDate.of(2024, 2, 12)),
                CardConsumeData(realConsume = 6.5f, expectedConsume = 6f, unit = "kg", mes = 3, date = LocalDate.of(2024, 3, 10)),
                CardConsumeData(realConsume = 8.5f, expectedConsume = 8f, unit = "kg",mes = 4, date = LocalDate.of(2024, 4, 8)),
                CardConsumeData(realConsume = 9f, expectedConsume = 8.5f, unit = "kg", mes = 5, date = LocalDate.of(2024, 5, 6)),
                CardConsumeData(realConsume = 10f, expectedConsume = 9.5f, unit = "kg", mes = 6, date = LocalDate.of(2024, 6, 11)),
                CardConsumeData(realConsume = 11f, expectedConsume = 10.5f, unit = "kg", mes = 7, date = LocalDate.of(2024, 7, 15)),
                CardConsumeData(realConsume = 12f, expectedConsume = 11.5f, unit = "kg", mes = 8, date = LocalDate.of(2024, 8, 12)),
                CardConsumeData(realConsume = 13f, expectedConsume = 12.5f, unit = "kg", mes = 9, date = LocalDate.of(2024, 9, 10)),
                CardConsumeData(realConsume = 14f, expectedConsume = 13f, unit = "kg", mes = 10, date = LocalDate.of(2024, 10, 7)),
                CardConsumeData(realConsume = 15f, expectedConsume = 14.5f, unit = "kg", mes = 11, date = LocalDate.of(2024, 11, 5)),
                CardConsumeData(realConsume = 16f, expectedConsume = 15.5f, unit = "kg", mes = 12, date = LocalDate.of(2024, 12, 20))
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
                text = "Histórico estimativa de Pegada de Carbono",
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
                modifier = Modifier
                    .width(320.dp)
                    .height(1.dp)
                    .background(color = MaterialTheme.colorScheme.surface)
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Column(
                    modifier = Modifier
                        .width(200.dp)
                        .height(48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Lista",
                        color = if(!switch) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable {
                                switch = !switch
                            }
                    )
                    if(!switch) {
                        HorizontalDivider(
                            modifier = Modifier
                                .height(3.dp)
                                .width(28.dp)
                                .background(color = MaterialTheme.colorScheme.primary)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .width(200.dp)
                        .height(48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Gráficos",
                        color = if(switch) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable {
                                switch = !switch
                            }
                    )
                    if (switch) {
                        HorizontalDivider(
                            modifier = Modifier
                                .height(3.dp)
                                .width(28.dp)
                                .background(color = MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if(!switch) {
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
                                Text(text = "Pegada Crescente")
                            }
                            Button(onClick = { sortType = SortType.REAL_CONSUME_DESC }) {
                                Text(text = "Pegada Decrescente")
                            }
                        }
                    }
                }

                // Carbon Footprint Section
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
        else{
            LineChartConsumption(mockData, "kg")
        }
    }
}
