package io.github.sustainow.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import io.github.sustainow.domain.model.UserProfile

@Composable
fun UserProfileCard(userProfile: UserProfile,onClick:()->Unit) {
    OutlinedCard(
        colors=CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ),
        modifier= Modifier.padding(4.dp).fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(32.dp),
        border= BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 8.dp,
            hoveredElevation = 10.dp,
            pressedElevation = 12.dp)) {

        Row(verticalAlignment = Alignment.CenterVertically){
            if (userProfile.profilePicture != null) {
                SubcomposeAsyncImage(
                    model = userProfile.profilePicture ?: Icons.Default.AccountCircle,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.requiredSize(48.dp, 48.dp),
                    loading = { LoadingModal() })
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.requiredSize(48.dp, 48.dp)
                )
            }
            Text(text = userProfile.fullName)
        }

    }
}