import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DrawerFootprintEstimate(
    mes: String,
    emissaoEsperada: String,
    unidadeMedida: String,
    modifier: Modifier,
    onClose: () -> Unit, // Função de callback para fechar o drawer
) {
    Box(
        modifier = Modifier
            .width(280.dp)
            .height(917.dp)
            .padding(12.dp)
            .background(Color(0xFFFFF3E0))
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            // Botão de fechar com ícone de "X"
            IconButton(
                onClick = onClose, // Chamando o callback para fechar o drawer
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Fechar",
                    tint = Color.Black
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            18.dp,
                            16.dp
                        ) // Padding geral de 18px em cima e embaixo, 16px nas laterais
                        .align(Alignment.Start), // Alinhamento à esquerda
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = mes,
                        style = TextStyle(
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Default, // Roboto
                            fontSize = 22.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Normal,
                            lineHeight = 28.sp,
                            letterSpacing = 0.sp,
                            color = Color(0xFF49454F) // Cor específica
                        ),
                        modifier = Modifier
                            .padding(vertical = 18.dp, horizontal = 16.dp)
                            .padding(horizontal = 12.dp)
                    )
                }

                Text(
                    text = "Pegada estimada: $emissaoEsperada $unidadeMedida",
                    style = TextStyle(
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Default,
                        fontSize = 14.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                        lineHeight = 20.sp,
                        letterSpacing = 0.1.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                HorizontalDivider(
                    modifier = Modifier.padding(top = 16.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDrawerFootprintEstimate() {
    DrawerFootprintEstimate(
        mes = "Dezembro",
        emissaoEsperada = "250",
        unidadeMedida = "kg",
        modifier = Modifier,
        onClose = {}
    )
}