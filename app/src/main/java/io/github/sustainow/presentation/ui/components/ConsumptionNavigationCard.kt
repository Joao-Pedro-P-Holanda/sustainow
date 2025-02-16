package io.github.sustainow.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.sustainow.R
import io.github.sustainow.presentation.ui.utils.RedirectButtonAction
import io.github.sustainow.routes.HistoricCarbonFootprint
import io.github.sustainow.routes.HistoricConsumeEnergy
import io.github.sustainow.routes.HistoricConsumeWater
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ConsumptionNavigationCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    iconPainter: Painter,
    actions: List<RedirectButtonAction>,
    route: String,
    navController: NavController
) {
    val interactionSource = remember { MutableInteractionSource() }
    var showMessage by remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collectLatest { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    showMessage = true
                }
                is PressInteraction.Release -> {
                    showMessage = false
                }
            }
        }
    }

    ElevatedCard(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = MaterialTheme.shapes.small,
    ) {
        Box(modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                )

                Image(
                    painter = iconPainter,
                    contentDescription = null,
                    modifier = Modifier.size(148.dp),
                )
            }

            Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.TopEnd),
                ) {
                IconButton(
                    interactionSource = interactionSource,
                    onClick = {
                        // Navegar para o destino baseado no parâmetro route
                        when (route) {
                            "water_consumption" -> navController.navigate(HistoricConsumeWater)
                            "energy_consumption" -> navController.navigate(HistoricConsumeEnergy)
                            "carbon_footprint" -> navController.navigate(HistoricCarbonFootprint)
                            else -> {
                            }
                        }
                    },
                    modifier = Modifier.size(24.dp),
                    content = {
                        Icon(
                            imageVector = Icons.Default.History,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = "Histórico"
                        )
                    },
                )

                if (showMessage) {
                    Text(
                        text = "Ver Histórico",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(4.dp)
                    )
                }
            }
        }

        Row(modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Row(modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.End) {
            for (action in actions) {
                Button(
                    onClick = { action.callback() },
                    colors = ButtonColors(
                        containerColor = action.color,
                        contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                        disabledContainerColor = action.color.copy(alpha = 0.5f),
                        disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
                    ),
                ) {
                    Text(text = action.text)
                }
                Spacer(modifier.size(8.dp))
            }
        }
    }
}

@Composable
@Preview
fun ConsumptionNavigationCardPreview() {
    ConsumptionNavigationCard(
        title = "Energy",
        description = "This is the energy card, blabla blalsjodiasjfoaiphfiashfpaoihrawporhawoirhawopraowh",
        iconPainter = painterResource(id = R.drawable.lightbulb),
        actions =
        listOf(
            RedirectButtonAction(
                text = "Real",
                callback = {},
                color = MaterialTheme.colorScheme.primary,
            ),
            RedirectButtonAction(
                text = "Expected",
                callback = {},
                color = MaterialTheme.colorScheme.tertiary,
            ),
        ),
        navController = rememberNavController(),
        route = "carbon_footprint"
    )
}
