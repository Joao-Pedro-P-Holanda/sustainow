package io.github.sustainow.presentation.ui.utils

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import io.github.sustainow.domain.model.CardConsumeData
import java.time.format.DateTimeFormatter

@Composable
fun LineChartConsumption(
    data: List<CardConsumeData>,
    consumptionMetric: String
){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
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

            Text(text = "Consumo real (${consumptionMetric})")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row{
            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(MaterialTheme.colorScheme.secondary, CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))

            Text(text = "Consumo esperado (${consumptionMetric})")
        }
    }

    val updateData = listOf(CardConsumeData(
        expectedConsume = 0f,
        realConsume = 0f,
        unit = "kwh",
        mes = 0,
        date = "/",
    )) + data

    val realConsumptionPoints = updateData.mapIndexed{ index, it -> Point(index.toFloat(), it.realConsume) }
    val expectedConsumptionPoints = updateData.mapIndexed{ index, it -> Point(index.toFloat(), it.expectedConsume) }

    val minY = minOf(
        updateData.minOfOrNull { it.realConsume } ?: 0f,
        updateData.minOfOrNull { it.expectedConsume } ?: 0f
    )
    val maxY = maxOf(
        updateData.maxOfOrNull { it.realConsume } ?: 0f,
        updateData.maxOfOrNull { it.expectedConsume } ?: 0f
    )
    val yRange = maxY - minY

    val linePlotData = LinePlotData(
        lines = listOf(
            Line(
                dataPoints = expectedConsumptionPoints,
                lineStyle = LineStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    lineType = LineType.Straight(isDotted = false)
                ),
                intersectionPoint = IntersectionPoint(
                    color = MaterialTheme.colorScheme.secondary
                ),
            ),
            Line(
                dataPoints = realConsumptionPoints,
                lineStyle = LineStyle(
                    color = MaterialTheme.colorScheme.tertiary,
                    lineType = LineType.Straight(isDotted = false)
                ),
                intersectionPoint = IntersectionPoint(
                    color = MaterialTheme.colorScheme.tertiary
                ),
            ),
        )
    )

    val lineChartData = LineChartData(
        linePlotData =linePlotData,
        xAxisData = AxisData.Builder()
            .steps(updateData.size - 1)
            .axisStepSize(50.dp)
            .labelData { index ->
                if (index < updateData.size) {
                    val dateFormat = updateData[index].date

                    val stringSplit = dateFormat.split("/")

                    stringSplit[0] + "/${stringSplit[1].takeLast(2)}"
                }
                else ""
            }
            .axisLabelAngle(0f)
            .axisLineColor(MaterialTheme.colorScheme.onSurface)
            .axisLabelColor(MaterialTheme.colorScheme.onSurface)
            .build(),
        yAxisData = AxisData.Builder()
            .steps(8)
            .labelData { i ->

                val increment = yRange / 8
                val labelValue = minY + (increment * i)

                "%.1f".format(labelValue)
            }
            .axisLineColor(MaterialTheme.colorScheme.onSurface)
            .axisLabelColor(MaterialTheme.colorScheme.onSurface)
            .build(),
        backgroundColor = MaterialTheme.colorScheme.surface,
        gridLines = GridLines(color = MaterialTheme.colorScheme.onSurface),
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
            lineChartData = lineChartData,
        )
    }
}