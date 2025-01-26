package io.github.sustainow.presentation.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import io.github.sustainow.R
import io.github.sustainow.domain.model.ActivityType
import io.github.sustainow.domain.model.MemberActivity
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDate

@Composable
fun ActivityDescription(activity: MemberActivity, removable:Boolean=false,onRemove:()->Unit={}) {
    var optionsRowWidth by remember{
        mutableFloatStateOf(0f)
    }
    val offset = remember{
        Animatable(initialValue = 0f)
    }
    val scope = rememberCoroutineScope()

    Box(modifier=Modifier.fillMaxWidth()){
        Row(modifier=Modifier.onSizeChanged { optionsRowWidth = it.width.toFloat() }){
            if(removable){
                IconButton(onClick = onRemove,
                    colors= IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier=Modifier.padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.requiredSize(24.dp, 24.dp),
                        tint=MaterialTheme.colorScheme.onError
                    )
                }
            }
        }
        Surface(modifier= Modifier
            .fillMaxWidth()
            .offset{ IntOffset(offset.value.toInt(),0) }
            .pointerInput(true){
                detectHorizontalDragGestures(
                    onHorizontalDrag =  { _, dragAmount ->
                        scope.launch{
                            val newOffset = (offset.value+dragAmount).coerceIn(0f,optionsRowWidth)
                            offset.snapTo(newOffset)
                        }
                    },
                    onDragEnd = {
                        when{
                            offset.value >= optionsRowWidth / 2f -> {
                                scope.launch {
                                    offset.animateTo(optionsRowWidth)
                                }
                            }
                            else ->{
                                scope.launch {
                                    offset.animateTo(0f)
                                }
                            }
                        }
                    })
            }){
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically) {
                if (activity.memberProfile.profilePicture != null) {
                    SubcomposeAsyncImage(
                        model = activity.memberProfile.profilePicture,
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
                Column(modifier = Modifier.fillMaxWidth()) {
                    val monthYearFormat = LocalDate.Format {
                        monthNumber(); char('/'); yearTwoDigits(1970)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(titleForActivity(activity), style = MaterialTheme.typography.titleSmall)
                        Text(
                            monthYearFormat.format(
                                activity.date.toJavaLocalDateTime().toLocalDate().toKotlinLocalDate()
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.widthIn(30.dp, 60.dp)
                        )
                    }
                    if (activity.comment != null) {
                        Text(activity.comment, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun titleForActivity(activity: MemberActivity): String {
    return when (activity.type) {
        ActivityType.COMMENT -> stringResource(R.string.activity_comment_title, formatArgs = arrayOf(activity.memberProfile.fullName))
        ActivityType.JOIN -> stringResource(R.string.activity_joined_title, formatArgs = arrayOf(activity.memberProfile.fullName))
        ActivityType.CHANGE_STATUS -> stringResource(R.string.activity_changed_status_title, formatArgs = arrayOf(activity.memberProfile.fullName))
    }
}