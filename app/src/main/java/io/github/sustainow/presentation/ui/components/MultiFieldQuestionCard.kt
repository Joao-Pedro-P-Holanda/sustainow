package io.github.sustainow.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import io.github.sustainow.domain.model.Question
import io.github.sustainow.domain.model.QuestionAlternative
import io.github.sustainow.presentation.theme.AppTheme
import kotlin.time.Duration

@Composable
fun MultiFieldQuestionCard(
    multiItem: Question.MultiItem,
){
    var isExpanded by remember { mutableStateOf(false) }
    val rotationIcon by animateFloatAsState(
        targetValue = if(isExpanded) 180f else 0f,
        label = ""
    )

    Card(
        modifier = Modifier
            .width(400.dp)
        ,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        )
    ) {
        Column (
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 2.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    multiItem.name ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Edit,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp).aspectRatio(1f)
                    )
                }

                Spacer(modifier = Modifier.width(73.dp))

                BasicTextField(
                    modifier = Modifier
                        .height(28.dp)
                        .width(24.dp),
                    textStyle = TextStyle(
                        fontSize = 8.sp,
                        textAlign = TextAlign.Center,
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small),
                            contentAlignment = Alignment.Center
                        ){
                            innerTextField()
                        }
                    },
                    value = "Qnt",
                    onValueChange = {}
                )

                Spacer(modifier = Modifier.width(10.dp))

                BasicTextField(
                    modifier = Modifier
                        .height(28.dp)
                        .width(58.dp),
                    textStyle = TextStyle(
                        fontSize = 8.sp,
                        textAlign = TextAlign.Center,
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small),
                            contentAlignment = Alignment.Center
                        ){
                            innerTextField()
                        }
                    },
                    value = "Tempo de uso",
                    onValueChange = {}
                )

                Spacer(modifier = Modifier.width(10.dp))

                BasicTextField(
                    modifier = Modifier
                        .height(28.dp)
                        .width(30.dp),
                    textStyle = TextStyle(
                        fontSize = 8.sp,
                        textAlign = TextAlign.Center,
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small),
                            contentAlignment = Alignment.Center
                        ){
                            innerTextField()
                        }
                    },
                    value = "Valor (W)",
                    onValueChange = {}
                )

                Icon(
                    Icons.Outlined.ArrowDropDown,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(rotationIcon)
                        .clickable { isExpanded = !isExpanded }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false}
            ) {
                DropdownMenuItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {},
                    text = { Text("") },
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .height(48.dp)
                    .width(48.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, shape = CircleShape)
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Add,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
@Preview
fun MultiFieldQuestionCardPreview(){
    val question = Question.MultiItem(
        text = "How much electronics consumption for week",
        name = "Geladeira",
        alternatives = mutableListOf(
            QuestionAlternative(id = 1, "carbon", text = "1", value = 20f, timePeriod = Duration.ZERO, unit = "bags"),
            QuestionAlternative(id = 2, "carbon", text = "2", value = 40f, timePeriod = Duration.ZERO, unit = "bags"),
            QuestionAlternative(id = 3, "carbon", text = "3", value = 60f, timePeriod = Duration.ZERO, unit = "bags"),
            QuestionAlternative(id = 4, "carbon", text = "4", value = 80f, timePeriod = Duration.ZERO, unit = "bags"),
            ),
        dependencies = emptyList()
    )
    AppTheme {
        MultiFieldQuestionCard(question)
    }
}


