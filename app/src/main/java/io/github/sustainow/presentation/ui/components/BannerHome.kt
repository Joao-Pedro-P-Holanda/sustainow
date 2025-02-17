package io.github.sustainow.presentation.ui.components

import ExpectedCarbonFootprint
import RealEnergyConsumption
import RealWaterConsumption
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter

@Composable
fun BannerHome(
    value: Number, unit: String, date: LocalDate?,
    type: String, navController: NavController,
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val title = when (type) {
        "carbon_footprint" -> "Minha Pegada de Carbono"
        "water_consume" -> "Meu Consumo de Água"
        "energy_consume" -> "Meu Consumo de Energia"
        else -> "Minha Pegada"
    }

    val route = when (type) {
        "carbon_footprint" -> ExpectedCarbonFootprint
        "water_consume" -> RealWaterConsumption
        "energy_consume" -> RealEnergyConsumption
        else -> ExpectedCarbonFootprint
    }

    val item = Triple(title, Triple(value, unit, date), route)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (value == 0) {
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .padding(10.dp)
                    .border(1.dp, color = MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = FontFamily.Serif,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 32.sp,
                        letterSpacing = 0.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Não preenchido esse mês",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = FontFamily.Serif,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp,
                        letterSpacing = 0.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Button(
                        onClick = { navController.navigate(route) },
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        Text("Calcular")
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val (title, values, route) = item
                val (current, unit, date) = values

                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .padding(10.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = title,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = FontFamily.Serif,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 32.sp,
                            letterSpacing = 0.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "$current $unit",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = FontFamily.Serif,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 32.sp,
                            letterSpacing = 0.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        if (date != null) {
                            Text(
                                text = "Medida pela última vez em: ${
                                    date.toJavaLocalDate().format(formatter)
                                }",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = FontFamily.Serif,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 20.sp,
                                letterSpacing = 0.1.sp,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                        } else {
                            Text(
                                text = "Sem data registrada",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = FontFamily.Serif,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 20.sp,
                                letterSpacing = 0.1.sp,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                        }

                        Button(
                            onClick = { navController.navigate(route) },
                            modifier = Modifier.padding(top = 10.dp)
                        ) {
                            Text("Calcular")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BannerHomePreview() {
    BannerHome(
        value = 0, unit = "kg", date = LocalDate(2024, 10, 21),
        type = "carbon_footprint", navController = rememberNavController()
    )
}
