package io.github.sustainow.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.sustainow.R
import io.github.sustainow.presentation.ui.utils.RedirectButtonAction

@Composable
fun ConsumptionNavigationCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    iconPainter: Painter,
    actions: List<RedirectButtonAction>,
) {
    ElevatedCard(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = MaterialTheme.shapes.small,
    ) {
        Row(
            modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier.fillMaxWidth().weight(1f),
            )

            Image(
                painter = iconPainter,
                contentDescription = null,
                modifier = Modifier.size(148.dp),
            )
        }
        Row(modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = modifier.fillMaxWidth(),
            )
        }

        Row(modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.End) {
            for (action in actions) {
                Button(
                    onClick = { action.callback() },
                    colors =
                        ButtonColors(
                            containerColor = action.color,
                            contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                            disabledContainerColor = action.color.copy(alpha = 0.5f),
                            disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
                        ),
                ) {
                    Text(text = action.text)
                }
                Spacer(modifier.size(8.dp))
            }
        }
    }
}

@Composable
@Preview
fun ConsumptionNavigationCardPreview() {
    ConsumptionNavigationCard(
        title = "Energy",
        description = "This is the energy card, blabla blalsjodiasjfoaiphfiashfpaoihrawporhawoirhawopraowh",
        iconPainter = painterResource(id = R.drawable.lightbulb),
        actions =
            listOf(
                RedirectButtonAction(
                    text = "Real",
                    callback = {},
                    color = MaterialTheme.colorScheme.primary,
                ),
                RedirectButtonAction(
                    text = "Expected",
                    callback = {},
                    color = MaterialTheme.colorScheme.tertiary,
                ),
            ),
    )
}
