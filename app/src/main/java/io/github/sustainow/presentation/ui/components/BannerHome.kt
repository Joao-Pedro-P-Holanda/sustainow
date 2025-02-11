package io.github.sustainow.presentation.ui.components

import ExpectedCarbonFootprint
import RealEnergyConsumption
import RealWaterConsumption
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter

@Composable
fun BannerHome(
    carbonValue: Number, carbonUnit: String, carbonDate: LocalDate,
    energyValue: Number, energyUnit: String, energyDate: LocalDate,
    waterValue: Number, waterUnit: String, waterDate: LocalDate,
    navController: NavController
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val itemsList = listOf(
        Triple("Minha Pegada de Carbono", Triple(carbonValue, carbonUnit, carbonDate), ExpectedCarbonFootprint),
        Triple("Meu Consumo de Energia", Triple(energyValue, energyUnit, energyDate), RealEnergyConsumption),
        Triple("Meu Consumo de Água", Triple(waterValue, waterUnit, waterDate), RealWaterConsumption)
    )

    var currentIndex by remember { mutableStateOf(0) }

    // Criando rolagem automática
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000) // Intervalo de 3 segundos
            currentIndex = (currentIndex + 1) % itemsList.size
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(itemsList) { (title, values, route) ->
                val (current, unit, date) = values

                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .padding(10.dp)
                        .border(1.dp, Color(0xFF6D7B6E), RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
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
                            color = Color(0xFF161D17),
                            fontFamily = FontFamily.Serif,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 32.sp,
                            letterSpacing = 0.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "$current $unit",
                            color = Color(0xFF161D17),
                            fontFamily = FontFamily.Serif,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 32.sp,
                            letterSpacing = 0.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "Medida pela última vez em: ${date.toJavaLocalDate().format(formatter)}",
                            color = Color(0xFF161D17),
                            fontFamily = FontFamily.Serif,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 20.sp,
                            letterSpacing = 0.1.sp,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

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
        carbonValue = 50, carbonUnit = "kg", carbonDate = LocalDate(2024, 10, 21),
        energyValue = 120, energyUnit = "kWh", energyDate = LocalDate(2024, 10, 20),
        waterValue = 200, waterUnit = "L", waterDate = LocalDate(2024, 10, 19),
        navController = rememberNavController()
    )
}