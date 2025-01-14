package io.github.sustainow.presentation.ui.utils

import android.text.TextUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import io.github.sustainow.domain.model.CardConsumeData

@Composable
fun PieChartConsumption(
    data: List<CardConsumeData>,
    metric: String
){
    val expectedSumValues = data.sumOf{ it.expectedConsume.toDouble() }
    val realSumValues = data.sumOf{ it.realConsume.toDouble() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ){
        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(MaterialTheme.colorScheme.tertiary, CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))

            Text(text = "$expectedSumValues $metric")
        }

        Spacer(modifier = Modifier.width(10.dp))

        Row{
            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(MaterialTheme.colorScheme.secondary, CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))

            Text(text = "$realSumValues $metric")
        }
    }

    val pieChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice(
                label = "%.2f".format(expectedSumValues) + metric,
                value = expectedSumValues.toFloat(),
                color = MaterialTheme.colorScheme.secondary,
            ),
            PieChartData.Slice(
                label = "%.2f".format(realSumValues) + metric,
                value = realSumValues.toFloat(),
                color = MaterialTheme.colorScheme.tertiary,
            ),
        ),
        plotType = PlotType.Pie
    )

    val pieChartConfig = PieChartConfig(
        isAnimationEnable = true,
        showSliceLabels = false,
        activeSliceAlpha = 0.5f,
        backgroundColor = MaterialTheme.colorScheme.surface,
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ){
        PieChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
            pieChartData = pieChartData,
            pieChartConfig = pieChartConfig,
        )
    }
}