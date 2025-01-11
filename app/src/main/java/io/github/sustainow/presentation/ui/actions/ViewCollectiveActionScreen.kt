package io.github.sustainow.presentation.ui.actions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import io.github.sustainow.R
import io.github.sustainow.presentation.viewmodel.CollectiveActionViewModel
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter


@Composable
fun CollectiveActionScreen(viewModel:CollectiveActionViewModel,modifier: Modifier =Modifier){
    val action by viewModel.action.collectAsState()
    if (action != null){
        Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
            Image(
                painter = rememberAsyncImagePainter(
                    action!!.images.firstOrNull() ?: R.drawable.image_not_found,
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = action!!.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = action!!.author,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(8.dp)
            )
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Text(
                    text = action!!.startDate.toJavaLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = action!!.endDate.toJavaLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Text(
                text = action!!.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }

}