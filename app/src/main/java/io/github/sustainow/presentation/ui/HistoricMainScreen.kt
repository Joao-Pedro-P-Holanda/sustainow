package io.github.sustainow.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.sustainow.R
import io.github.sustainow.presentation.ui.components.ConsumptionNavigationCard
import io.github.sustainow.presentation.ui.utils.RedirectButtonAction
import io.github.sustainow.routes.HistoricCarbonFootprint
import io.github.sustainow.routes.HistoricConsumeEnergy
import io.github.sustainow.routes.HistoricConsumeWater

@Composable
fun HistoricMainScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier =
        modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.historic_main_screen_title),
            style = MaterialTheme.typography.displaySmall,
        )
        Text(
            text = stringResource(R.string.historic_main_screen_description),
            style = MaterialTheme.typography.bodyMedium,
        )

        HorizontalDivider(modifier.fillMaxWidth())

        Column(
            modifier =
            modifier
                .verticalScroll(scrollState)
                .graphicsLayer(
                    compositingStrategy = androidx.compose.ui.graphics.CompositingStrategy.Offscreen,
                )
                .drawWithContent {
                    val colors = listOf(Color.Transparent, Color.Black)
                    if (scrollState.canScrollBackward) {
                        drawRect(
                            brush = Brush.verticalGradient(colors),
                            size = Size(size.width, 20.0f),
                            blendMode = BlendMode.DstIn,
                        )
                    }
                    drawContent()
                    if (scrollState.canScrollForward) {
                        drawRect(
                            brush = Brush.verticalGradient(colors),
                            size = Size(size.width, 20.0f),
                            blendMode = BlendMode.DstIn,
                        )
                    }
                },
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ConsumptionNavigationCard(
                modifier,
                title = stringResource(id = R.string.historic_navigate_carbon_title),
                description = stringResource(id = R.string.historic_navigate_carbon_description),
                iconPainter = painterResource(id = R.drawable.carbon_footprint),
                actions =
                listOf(
                    RedirectButtonAction(
                        text = stringResource(id = R.string.historic_navigate_view_text),
                        callback = { navController.navigate(HistoricCarbonFootprint) },
                        MaterialTheme.colorScheme.primary,
                    ),
                ),
            )
            ConsumptionNavigationCard(
                modifier,
                title = stringResource(id = R.string.historic_navigate_energy_title),
                description = stringResource(id = R.string.historic_navigate_energy_description),
                iconPainter = painterResource(id = R.drawable.lightbulb),
                actions =
                listOf(
                    RedirectButtonAction(
                        text = stringResource(id = R.string.historic_navigate_view_text),
                        callback = { navController.navigate(HistoricConsumeEnergy) },
                        color =
                        MaterialTheme.colorScheme.primary,
                    ),
                ),
            )
            ConsumptionNavigationCard(
                modifier,
                title = stringResource(id = R.string.historic_navigate_water_title),
                description = stringResource(id = R.string.historic_navigate_water_description),
                iconPainter = painterResource(id = R.drawable.water_drop),
                actions =
                listOf(
                    RedirectButtonAction(
                        text = stringResource(id = R.string.historic_navigate_view_text),
                        callback = { navController.navigate(HistoricConsumeWater) },
                        color =
                        MaterialTheme.colorScheme.primary,
                    ),
                ),
            )
        }
    }
}
