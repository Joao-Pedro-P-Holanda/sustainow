package io.github.sustainow.presentation.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.presentation.ui.components.LoadingModal
import io.github.sustainow.presentation.viewmodel.HomeViewModel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import io.github.sustainow.R
import io.github.sustainow.domain.model.LabeledImage
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    userState: UserState,
    redirectLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val context = LocalContext.current
    val videoUri = "android.resource://${context.packageName}/${R.raw.fogueira}"

    val items = listOf(
        LabeledImage(
            image = loadBitmapFromResource(context, R.drawable.pegadacarbono),
            label = stringResource(R.string.carbon_footprint),
            supportingText = stringResource(R.string.carbon_footprint)
        ),
        LabeledImage(
            image = loadBitmapFromResource(context, R.drawable.economiaagua),
            label = stringResource(R.string.water_saving),
            supportingText = stringResource(R.string.water_saving)
        ),
        LabeledImage(
            image = loadBitmapFromResource(context, R.drawable.economiaenergia),
            label = stringResource(R.string.energy_saving),
            supportingText = stringResource(R.string.energy_saving)
        ),
        LabeledImage(
            videoUrl = videoUri,
            label = stringResource(R.string.carbon_footprint_video),
            supportingText = stringResource(R.string.carbon_footprint_video),
        )
    )

    Log.i("HomeScreen", "UserState: $userState")
    when (userState) {
        is UserState.NotLogged -> {
            redirectLogin()
        }
        is UserState.Loading -> {
            LoadingModal()
        }
        is UserState.Logged -> {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = { viewModel.signOut() }) {
                    Text(text = "Logout")
                }
                HorizontalMultiBrowseCarousel(
                    state = rememberCarouselState { items.count() },
                    modifier = Modifier.width(350.dp).height(250.dp),
                    preferredItemWidth = 350.dp,
                    itemSpacing = 16.dp,
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) { i ->
                    val item = items[i]
                    Box(
                        modifier = Modifier
                            .height(180.dp)
                            .width(350.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        if (item.videoUrl != null) {
                            // Video Player
                            VideoPlayer(
                                videoUrl = item.videoUrl,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else if (item.image != null) {
                            // Image Display
                            Image(
                                painter = BitmapPainter(item.image.asImageBitmap()),
                                contentDescription = item.label,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize().graphicsLayer(alpha = 0.5f)
                            )
                        }

                        // Overlapping Texts
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = item.supportingText,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                ),
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }

            }
    }
}
}
fun loadBitmapFromResource(context: Context, resId: Int): Bitmap {
    return BitmapFactory.decodeResource(context.resources, resId)
}

@Composable
fun VideoPlayer(videoUrl: String, modifier: Modifier) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
                useController = true // Exibe os controles de reprodução
            }
        },
        modifier = modifier
    )
}
