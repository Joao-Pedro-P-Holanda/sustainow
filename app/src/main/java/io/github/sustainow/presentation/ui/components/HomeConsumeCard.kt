package io.github.sustainow.presentation.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.sustainow.routes.HistoricConsumeEnergy
import io.github.sustainow.routes.HistoricConsumeWater
import kotlin.math.abs

@Composable
fun HomeConsumeCard(
    energyValue: Number, energyPrevious: Number, energyUnit: String,
    waterValue: Number, waterPrevious: Number, waterUnit: String,
    navController: NavController
) {

    Card(
        modifier = Modifier
            .width(372.dp)
            .padding(vertical = 10.dp)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Meu consumo", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)

            listOf(
                Triple("Consumo de Energia", Triple(energyValue, energyPrevious, energyUnit), HistoricConsumeEnergy),
                Triple("Consumo de Água", Triple(waterValue, waterPrevious, waterUnit), HistoricConsumeWater)
            ).forEach { (title, values, route) ->
                val (current, previous, unit) = values
                val percentageChange = if (previous.toDouble() != 0.0) {
                    ((current.toDouble() - previous.toDouble()) / previous.toDouble()) * 100
                } else 0.0
                val isIncrease: Boolean? = when {
                    percentageChange > 0 -> true
                    percentageChange < 0 -> false
                    else -> null
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Button(
                        onClick = { navController.navigate(route) },
                    ) {
                        Icon(Icons.Default.Today, contentDescription = "Histórico")
                        Spacer(Modifier.width(2.dp))
                        Text("Ver histórico")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Mês Atual", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Text(text = "$current $unit", fontSize = 16.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if(energyPrevious == 0 ) {
                            Text(
                                text = "Não preenchido no mês anterio",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = FontFamily.Serif,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 20.sp,
                                letterSpacing = 0.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        } else {
                            Text(text = "${"%.2f".format(abs(percentageChange))}%", fontSize = 14.sp)
                            if(isIncrease != null) {
                                Icon(
                                    imageVector = if (isIncrease == true) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                                    contentDescription = if (isIncrease == true) "Aumento" else "Diminuição",
                                    tint = if (isIncrease == true) Color.Red else Color.Green
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeConsumeCard() {
    HomeConsumeCard(
        energyValue = 120, energyPrevious = 130, energyUnit = "kWh",
        waterValue = 200, waterPrevious = 0, waterUnit = "m³",
        navController = rememberNavController()
    )
}
