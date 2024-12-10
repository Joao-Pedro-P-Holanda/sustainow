package io.github.sustainow.presentation.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.presentation.ui.components.LoadingModal
import io.github.sustainow.presentation.viewmodel.HomeViewModel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.sustainow.R
import io.github.sustainow.domain.model.LabeledImage

/*
* HomeScreen is the main screen of the application.
* TODO: remove logout button when the scope is finished
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    userState: UserState,
    redirectLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val items = listOf(
        LabeledImage(R.drawable.pegadacarbono, "Pegada Carbono", "Pegada Carbono"),
        LabeledImage(R.drawable.economiaagua, "Economia de água", "Economia de água"),
        LabeledImage(R.drawable.economiaenergia, "Economia de energia", "Economia de energia"),
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
                verticalArrangement = Arrangement.Center,
            ) {
                Button(onClick = { viewModel.signOut() }) {
                    Text(text = "Logout")
                }
                HorizontalMultiBrowseCarousel(
                    state = rememberCarouselState { items.count() },
                    modifier = Modifier.width(412.dp).height(300.dp), // Ajuste para acomodar os textos
                    preferredItemWidth = 412.dp, // Largura suficiente para mostrar apenas um item por vez
                    itemSpacing = 0.dp, // Remover o espaçamento
                    contentPadding = PaddingValues(horizontal = 0.dp)
                ) { i ->
                    val item = items[i]
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        // Exibir o título (label)
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        // Exibir o subtítulo (supportingText)
                        Text(
                            text = item.supportingText,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        // Exibir a imagem
                        Image(
                            modifier = Modifier
                                .height(205.dp)
                                .maskClip(MaterialTheme.shapes.extraLarge),
                            painter = painterResource(id = item.image),
                            contentDescription = item.label,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}
