package io.github.sustainow.presentation.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import io.github.sustainow.R
import io.github.sustainow.domain.model.LabeledImage
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.presentation.ui.components.BannerHome
import io.github.sustainow.presentation.ui.components.HomeConsumeCard
import io.github.sustainow.presentation.ui.components.LoadingModal
import io.github.sustainow.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController,
    userState: UserState,
    redirectLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val videoUri = "android.resource://${context.packageName}/${R.raw.fogueira}"

    val items = listOf(
        LabeledImage(
            image = loadBitmapFromResource(context, R.drawable.carbon_footprint2),
            label = stringResource(R.string.carbon_footprint),
            supportingText = "Uma única árvore pode absorver até 22 kg de CO₂ por ano e liberar oxigênio suficiente para duas pessoas.",
        ),
        LabeledImage(
            image = loadBitmapFromResource(context, R.drawable.energy_consume),
            label = stringResource(R.string.energy_saving),
            supportingText = "Dispositivos conectados, mesmo desligados, podem representar até 10% da conta de luz.",
        ),
        LabeledImage(
            image = loadBitmapFromResource(context, R.drawable.water_consume),
            label = stringResource(R.string.water_saving),
            supportingText = " Se você toma um banho de 10 minutos, pode gastar 90 litros de água. Reduzindo para 5 minutos, economiza quase 50 litros por banho!",
        ),
        LabeledImage(
            videoUrl = videoUri,
            label = stringResource(R.string.carbon_footprint),
            supportingText = "Queimar madeira ou restos de plantas pode parecer ecológico, mas pode liberar mais CO₂ do que combustíveis fósseis se não for feito com controle adequado."
        ),
    )

    // Observa os valores do ViewModel
    val carbonFootprintCurrent by viewModel.carbonFootprintCurrent.collectAsState()
    val energyConsumeCurrent by viewModel.energyConsumeCurrent.collectAsState()
    val energyConsumePrevious by viewModel.energyConsumePrevious.collectAsState()
    val waterConsumeCurrent by viewModel.waterConsumeCurrent.collectAsState()
    val waterConsumePrevious by viewModel.waterConsumePrevious.collectAsState()
    val carbonFootprintDate by viewModel.carbonFootprintDate.collectAsState()
    val error by viewModel.error.collectAsState()

    when (userState) {
        is UserState.NotLogged -> redirectLogin()
        is UserState.Loading -> LoadingModal()
        is UserState.Logged -> {
            Column(
                modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Carousel de imagens e vídeo
                HorizontalMultiBrowseCarousel(
                    state = rememberCarouselState { items.count() },
                    modifier = Modifier.width(350.dp).height(250.dp),
                    preferredItemWidth = 350.dp,
                    itemSpacing = 16.dp,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) { i ->
                    val item = items[i]
                    Box(
                        modifier = Modifier.height(180.dp).width(350.dp).clip(RoundedCornerShape(16.dp)),
                    ) {
                        if (item.videoUrl != null) {
                            VideoPlayer(videoUrl = item.videoUrl, modifier = Modifier.fillMaxSize())
                        } else if (item.image != null) {
                            Image(
                                painter = BitmapPainter(item.image.asImageBitmap()),
                                contentDescription = item.label,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize().graphicsLayer(alpha = 0.5f),
                            )
                        }
                        Column(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(bottom = 4.dp),
                            )
                            Text(
                                text = item.supportingText,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                ),
                                textAlign = TextAlign.Start,
                            )
                        }
                    }
                }

                BannerHome(
                    value = carbonFootprintCurrent?.total ?: 0,
                    unit = "kg",
                    date = carbonFootprintDate,
                    type = "carbon_footprint",
                    navController = navController,
                )

                if(error == null) {
                    HomeConsumeCard(
                        energyValue = energyConsumeCurrent?.total ?: 0,
                        energyPrevious = energyConsumePrevious?.total ?: 0,
                        energyUnit = "kWh",
                        waterValue = waterConsumeCurrent?.total ?: 0,
                        waterPrevious = waterConsumePrevious?.total ?: 0,
                        waterUnit = "m³",
                        navController = navController
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Card(
                            modifier = Modifier
                                .width(372.dp)
                                .padding(vertical = 10.dp)
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp)),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
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
                                    text = "Erro ao carregar dados",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    text = error?.message
                                        ?: "Algo deu errado. Tente novamente mais tarde.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


fun loadBitmapFromResource(
    context: Context,
    resId: Int,
): Bitmap {
    return BitmapFactory.decodeResource(context.resources, resId)
}

@Composable
fun VideoPlayer(
    videoUrl: String,
    modifier: Modifier,
) {
    val context = LocalContext.current
    val exoPlayer =
        remember {
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
        modifier = modifier,
    )
}
