package io.github.sustainow.presentation.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import io.github.sustainow.R
import io.github.sustainow.domain.model.CollectiveAction
import io.github.sustainow.presentation.ui.utils.uriToImageBitmap

@Composable
fun CollectiveActionCard(action: CollectiveAction, redirectAction: ()->Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val uri = action.images.firstOrNull()
            if (uri!= null) {
                SubcomposeAsyncImage(
                    model=uri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(0.dp,300.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    loading={ LoadingModal() }
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.image_not_found),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = action.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = action.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier=Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                AssistChip(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    onClick = { /* Handle chip click */ },
                    label = { Text(text = action.status) },
                    enabled = false,
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = when (action.status) {
                            "Em andamento" -> MaterialTheme.colorScheme.primary
                            "Finalizada" -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.surface
                        }
                    )
                )
                Button(onClick = {redirectAction()}) {
                    Text(text = "Ver detalhes")
                }
            }

        }
    }
}