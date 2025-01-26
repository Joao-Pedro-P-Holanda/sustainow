package io.github.sustainow.presentation.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.sustainow.R
import io.github.sustainow.domain.model.Invitation

@Composable
fun InvitationCard(invite:Invitation, answerCallback: (Boolean)->Unit){
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    ElevatedCard (
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp, hoveredElevation = 10.dp, pressedElevation = 12.dp),
        colors = CardColors(
            containerColor = if (isHovered) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.surfaceContainerLowest,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            disabledContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = Modifier.padding(4.dp).fillMaxWidth(0.8f).shadow(
            elevation=10.dp,
            shape = RoundedCornerShape(12.dp)
        ),
    ) {
        Column(modifier=Modifier.padding(12.dp)){
        Text(
            stringResource(
               id= R.string.invitation_card_title,
                formatArgs = arrayOf(invite.actionName)
            )
        )
        Row(horizontalArrangement = Arrangement.SpaceAround, modifier=Modifier.fillMaxWidth()) {
            Button(onClick = {answerCallback(true)}){
                Text("Aceitar")
            }
            Button(onClick = {answerCallback(false)},
                   colors= ButtonColors(
                       containerColor = MaterialTheme.colorScheme.error,
                       contentColor = MaterialTheme.colorScheme.onError,
                       disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
                       disabledContentColor = MaterialTheme.colorScheme.onErrorContainer,
                   )
            ){
                Text("Recusar")
            }
        }
        }
    }

}

