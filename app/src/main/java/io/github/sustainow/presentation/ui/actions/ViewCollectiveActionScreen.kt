package io.github.sustainow.presentation.ui.actions

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ChipColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import io.github.sustainow.R
import io.github.sustainow.presentation.ui.components.LoadingModal
import io.github.sustainow.presentation.viewmodel.CollectiveActionViewModel
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter


@Composable
fun CollectiveActionScreen(viewModel:CollectiveActionViewModel,modifier: Modifier =Modifier, returnCallback: ()->Unit) {
    val action by viewModel.action.collectAsState()
    val loading by viewModel.loading.collectAsState()

    if(loading){
        LoadingModal()
    }
    else {
        if (action != null) {
            Column(
                modifier = modifier.fillMaxSize().padding(24.dp).verticalScroll( rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {

                Column(modifier=modifier.fillMaxWidth().clip(
                    RoundedCornerShape(16.dp)
                ).background(MaterialTheme.colorScheme.surfaceContainerHigh).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)){
                Image(
                    painter = rememberAsyncImagePainter(
                        action!!.images.firstOrNull() ?: R.drawable.image_not_found,
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(0.dp, 300.dp)
                        .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = action!!.name,
                    color=MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(8.dp)
                )

                HorizontalDivider(
                    modifier.fillMaxWidth(),
                )

                Text(
                    text = "${stringResource(R.string.view_collective_action_author_text)} ${action!!.author}",
                    color=MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(8.dp),
                    style=MaterialTheme.typography.labelLarge
                )
                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AssistChip(
                        onClick={},
                        label= {Text(
                            text = action!!.startDate.toJavaLocalDate()
                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            color=MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelMedium
                        )}
                    )
                    AssistChip(
                        onClick = {},
                    label = {Text(
                        text = action!!.endDate.toJavaLocalDate()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        color=MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelMedium
                    )})
                }
                Row (
                    modifier = Modifier.padding(8.dp),
                ){
                    AssistChip(
                        onClick = {},
                        label= {Text(
                            text = action!!.status,
                            color=MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelMedium
                        )},
                        border= BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
                    )
                }
                }


                Column(modifier=modifier.fillMaxWidth().clip(
                    RoundedCornerShape(16.dp)
                ).background(MaterialTheme.colorScheme.surfaceContainerHigh).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)){
                    Text(
                        text = stringResource(R.string.view_collective_action_description_header),
                        color=MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(8.dp)
                    )
                    HorizontalDivider(
                        modifier.fillMaxWidth(),
                    )
                Text(
                    text = action!!.description,
                    color=MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
                }
            }
        } else {
            Column(
                modifier = modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(stringResource(R.string.view_collective_action_not_found_text))
                Button(onClick = returnCallback) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    Text(stringResource(R.string.view_collective_action_back_text))
                }
            }
        }
    }
}