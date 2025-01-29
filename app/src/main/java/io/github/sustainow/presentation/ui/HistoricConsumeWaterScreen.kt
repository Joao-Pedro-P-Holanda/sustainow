package io.github.sustainow.presentation.ui

import DrawerConsume
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import io.github.sustainow.presentation.ui.components.DatePickerDialog
import io.github.sustainow.presentation.ui.components.HorizontalConsumeCard
import io.github.sustainow.presentation.ui.components.getMonthName
import io.github.sustainow.presentation.ui.utils.LineChartConsumption
import io.github.sustainow.presentation.ui.utils.groupAndSumByMonthYearWithStartEnd
import io.github.sustainow.presentation.viewmodel.HistoricViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoricConsumeWaterScreen(
    navController: NavController,
    viewModel: HistoricViewModel
) {
    val scrollState = rememberScrollState()
    var sortType by remember { mutableStateOf(SortType.DATE_ASC) }

    var switch by remember { mutableStateOf(false) }

    var mockData by remember {
        mutableStateOf(
            listOf(
                CardConsumeData(realConsume = 12f, expectedConsume = 10f, unit = "m³", mes = 1, date = "01/2024"),
                CardConsumeData(realConsume = 11f, expectedConsume = 9.5f, unit = "m³", mes = 2, date = "02/2024"),
                CardConsumeData(realConsume = 10f, expectedConsume = 9.5f, unit = "m³", mes = 3, date = "03/2024"),
                CardConsumeData(realConsume = 13f, expectedConsume = 12f, unit = "m³", mes = 4, date = "04/2024"),
                CardConsumeData(realConsume = 14f, expectedConsume = 13f, unit = "m³", mes = 5, date = "05/2024"),
                CardConsumeData(realConsume = 15f, expectedConsume = 14f, unit = "m³", mes = 6, date = "05/2024"),
                CardConsumeData(realConsume = 16f, expectedConsume = 15f, unit = "m³", mes = 7, date = "06/2024"),
                CardConsumeData(realConsume = 17f, expectedConsume = 16f, unit = "m³", mes = 8, date = "07/2024"),
                CardConsumeData(realConsume = 18f, expectedConsume = 17f, unit = "m³", mes = 9, date = "08/2024"),
                CardConsumeData(realConsume = 19f, expectedConsume = 18f, unit = "m³", mes = 10, date = "09/2024"),
                CardConsumeData(realConsume = 20f, expectedConsume = 19f, unit = "m³", mes = 11, date = "11/2024"),
                CardConsumeData(realConsume = 21f, expectedConsume = 20f, unit = "m³", mes = 12, date = "12/2024")
            )
        )
    }

    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    var showDrawer by remember { mutableStateOf(false) }
    var selectedCardData by remember { mutableStateOf<CardConsumeData?>(null) }

    val openDrawer = { cardData: CardConsumeData ->
        selectedCardData = cardData
        showDrawer = true
    }

    val closeDrawer = {
        showDrawer = false
    }

    val formulary by viewModel.formulary.collectAsState()

    val groupedData = formulary?.let { groupAndSumByMonthYearWithStartEnd(it) }?.toList()?.map { (pair, pairValue) ->
        CardConsumeData(
            expectedConsume = pairValue.first,
            realConsume = pairValue.second,
            unit = formulary!![0].unit,
            mes = pair.second,
            date = "${pair.second}/${pair.first}" // Ajustado para ser um LocalDate
        )
    } ?: emptyList()

    // Estado para armazenar a lista ordenada
    var sortedData by remember { mutableStateOf(groupedData) }

    // Aplicar ordenação quando sortType mudar
    LaunchedEffect(sortType, groupedData) {
        sortedData = groupedData.sortedWith(
            when (sortType) {
                SortType.DATE_ASC -> compareBy { it.date }
                SortType.DATE_DESC -> compareByDescending { it.date }
                SortType.REAL_CONSUME_ASC -> compareBy { it.realConsume }
                SortType.REAL_CONSUME_DESC -> compareByDescending { it.realConsume }
            }
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
                .fillMaxWidth()
                .height(141.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Histórico de Consumo de Água",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    fontSize = 32.sp,
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
                    // Filter Chips for date selection
                    Row(
                        modifier = Modifier.padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = showStartDatePicker,
                            onClick = { showStartDatePicker = !showStartDatePicker },
                            label = { Text("Data Inicial: ${startDate?.toString() ?: "Selecionar"}") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Today,
                                    contentDescription = null,
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            },
                            trailingIcon = if (startDate != null) {
                                {
                                    Icon(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = null,
                                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                                    )
                                }
                            } else null
                        )
                        if (showStartDatePicker) {
                            DatePickerDialog(
                                initialDate = startDate ?: LocalDate.now(),
                                onDateSelected = {
                                    startDate = it
                                    showStartDatePicker = false
                                },
                                onDismiss = { showStartDatePicker = false }
                            )
                        }

                        FilterChip(
                            selected = showEndDatePicker,
                            onClick = { showEndDatePicker = !showEndDatePicker },
                            label = { Text("Data Final: ${endDate?.toString() ?: "Selecionar"}") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Today,
                                    contentDescription = null,
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            },
                            trailingIcon = if (endDate != null) {
                                {
                                    Icon(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = null,
                                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                                    )
                                }
                            } else null
                        )
                        if (showEndDatePicker) {
                            DatePickerDialog(
                                initialDate = endDate ?: LocalDate.now(),
                                onDateSelected = {
                                    endDate = it
                                    showEndDatePicker = false
                                },
                                onDismiss = { showEndDatePicker = false }
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                sortType = if (sortType == SortType.DATE_ASC) SortType.DATE_DESC else SortType.DATE_ASC
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Data",
                                    modifier = Modifier.padding(end = 4.dp) // Espaço entre texto e ícone
                                )
                                Icon(
                                    imageVector = if (sortType == SortType.DATE_ASC) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                                    contentDescription = if (sortType == SortType.DATE_ASC) "Ordenar crescente" else "Ordenar decrescente"
                                )
                            }
                        }
                        Button(
                            onClick = {
                                sortType = if (sortType == SortType.REAL_CONSUME_ASC) SortType.REAL_CONSUME_DESC else SortType.REAL_CONSUME_ASC
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Valor",
                                    modifier = Modifier.padding(end = 4.dp) // Espaço entre texto e ícone
                                )
                                Icon(
                                    imageVector = if (sortType == SortType.REAL_CONSUME_ASC) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                                    contentDescription = if (sortType == SortType.REAL_CONSUME_ASC) "Ordenar crescente" else "Ordenar decrescente"
                                )
                            }
                        }
                    }
                }

                // Water Consumption Section
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
                    sortedData.forEach { data ->
                        HorizontalConsumeCard(
                            cardConsumeData = data,
                            onCardClick = { openDrawer(data) }
                        )
                    }
                }
            }
        }
        else{
            LineChartConsumption(mockData, "m³")
        }
    }
    AnimatedVisibility(
        visible = showDrawer,
        enter = fadeIn(animationSpec = tween(300)) + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(targetOffsetY = { it })
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize() // Faz o Box ocupar toda a tela
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)) // Fundo escurecido
        ) {
            DrawerConsume(
                mes = getMonthName(selectedCardData!!.mes),
                custoUnidade = 0.5f, // Substitua com o valor real
                consumoReal = "${selectedCardData!!.realConsume}",
                consumoEsperado = "${selectedCardData!!.expectedConsume}",
                unidadeMedida = selectedCardData!!.unit,
                modifier = Modifier
                    .align(Alignment.Center) // Centraliza o Drawer na tela
                    .fillMaxWidth() // Faz o Drawer ocupar toda a largura da tela
                    .height(300.dp) // Defina a altura desejada para o drawer
                    .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.large) // Estiliza o Drawer com fundo e bordas arredondadas
                    .padding(16.dp) // Adiciona o padding interno para conteúdo
                ,
                onClose = closeDrawer
            )
        }
    }
}
