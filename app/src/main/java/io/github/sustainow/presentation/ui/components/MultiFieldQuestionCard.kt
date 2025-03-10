package io.github.sustainow.presentation.ui.components

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import io.github.sustainow.R
import io.github.sustainow.domain.model.FormularyAnswerCreate
import io.github.sustainow.domain.model.Question
import io.github.sustainow.presentation.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Duration

@SuppressLint("MutableCollectionMutableState")
@Composable
fun MultiFieldQuestionCard(
    question: Question.MultiItem,
    editableNames: Boolean,
    onAnswerAdded: (FormularyAnswerCreate) -> Unit,
    onUpdateAnswer: (FormularyAnswerCreate) -> Unit,
    onQuestionNameChanged: (String) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    val rotationIcon by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "",
    )

    var subItems by remember { mutableStateOf(listOf<FormularyAnswerCreate>()) }

    var isEditHeader by remember { mutableStateOf(false) }

    Card(
        modifier =
            Modifier
                .width(400.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (isEditHeader) {
                    BasicTextField(
                        modifier =
                            Modifier
                                .height(28.dp)
                                .width(100.dp),
                        value = question.name ?: "",
                        onValueChange = { newName ->
                            onQuestionNameChanged(newName)
                            Log.i("", "${question.name}")
                        },
                        textStyle =
                            TextStyle(
                                fontSize = 8.sp,
                                textAlign = TextAlign.Start,
                            ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier =
                                    Modifier
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outline,
                                            MaterialTheme.shapes.small,
                                        ).padding(4.dp),
                                contentAlignment = Alignment.CenterStart,
                            ) {
                                innerTextField()
                            }
                        },
                    )
                    if (!isExpanded && editableNames) {
                        Box(
                            modifier =
                                Modifier
                                    .height(48.dp)
                                    .aspectRatio(1f)
                                    .clickable {
                                        isEditHeader = !isEditHeader
                                    },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                Icons.Outlined.Edit,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                contentDescription = null,
                                modifier =
                                    Modifier
                                        .size(24.dp)
                                        .aspectRatio(1f),
                            )
                        }
                    }
                } else {
                    Text(
                        question.name ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    if (!isExpanded && editableNames) {
                        Box(
                            modifier =
                                Modifier
                                    .height(48.dp)
                                    .aspectRatio(1f)
                                    .clickable {
                                        isEditHeader = !isEditHeader
                                    },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                Icons.Outlined.Edit,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                contentDescription = null,
                                modifier =
                                    Modifier
                                        .size(24.dp)
                                        .aspectRatio(1f),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BasicTextField(
                        modifier =
                            Modifier
                                .height(28.dp)
                                .width(24.dp),
                        textStyle =
                            TextStyle(
                                fontSize = 8.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface,
                            ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier =
                                    Modifier
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outline,
                                            MaterialTheme.shapes.small,
                                        ),
                                contentAlignment = Alignment.Center,
                            ) {
                                innerTextField()
                            }
                        },
                        value = if (!isExpanded) "Qnt" else "${subItems.size}",
                        onValueChange = {},
                        enabled = false,
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    BasicTextField(
                        modifier =
                            Modifier
                                .height(28.dp)
                                .width(58.dp),
                        textStyle =
                            TextStyle(
                                fontSize = 8.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface,
                            ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier =
                                    Modifier
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outline,
                                            MaterialTheme.shapes.small,
                                        ),
                                contentAlignment = Alignment.Center,
                            ) {
                                innerTextField()
                            }
                        },
                        value =
                            if (!isExpanded) {
                                "Tempo de uso"
                            } else {
                                "${subItems.sumOf { it.timePeriod?.inWholeSeconds ?: 0 } /
                                    maxOf(
                                        subItems.size,
                                        1,
                                    )
                                }"
                            },
                        onValueChange = {},
                        enabled = false,
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    BasicTextField(
                        modifier =
                            Modifier
                                .height(28.dp)
                                .width(30.dp),
                        textStyle =
                            TextStyle(
                                fontSize = 8.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface,
                            ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier =
                                    Modifier
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outline,
                                            MaterialTheme.shapes.small,
                                        ),
                                contentAlignment = Alignment.Center,
                            ) {
                                innerTextField()
                            }
                        },
                        value = if (!isExpanded) stringResource(R.string.collapsed_value) else "${subItems.sumOf { it.value.toDouble() }}",
                        onValueChange = {},
                        enabled = false,
                    )

                    Icon(
                        Icons.Outlined.ArrowDropDown,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null,
                        modifier =
                            Modifier
                                .rotate(rotationIcon)
                                .clickable {
                                    isExpanded = !isExpanded
                                    isEditHeader = false
                                },
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (isExpanded) {
                subItems.map { item ->
                    MultiFieldQuestionSubItem(
                        answer = item,
                        editableNames = editableNames,
                        onUpdateAnswer = {
                        },
                    )
                }
            }

            if (isExpanded) {
                IconButton(
                    modifier =
                        Modifier
                            .height(48.dp)
                            .width(48.dp)
                            .background(
                                MaterialTheme.colorScheme.secondaryContainer,
                                shape = CircleShape,
                            ).padding(vertical = 10.dp),
                    onClick = {
                    },
                ) {
                    Icon(
                        Icons.Outlined.Add,
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
fun MultiFieldQuestionSubItem(
    answer: FormularyAnswerCreate,
    editableNames: Boolean,
    onUpdateAnswer: (FormularyAnswerCreate) -> Unit,
) {
    var isEditSubItem by remember { mutableStateOf(false) }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceContainerLowest,
                    shape = MaterialTheme.shapes.small,
                ).padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isEditSubItem) {
            BasicTextField(
                modifier =
                    Modifier
                        .height(28.dp)
                        .width(100.dp),
                value = answer.groupName ?: "",
                onValueChange = { newText ->
                    val updatedName = if (newText == "") "Nome" else newText

                    onUpdateAnswer(
                        FormularyAnswerCreate(
                            questionId = answer.questionId,
                            groupName = updatedName,
                            uid = answer.uid,
                            value = answer.value,
                            unit = answer.unit,
                            timePeriod = answer.timePeriod,
                            formId = answer.formId,
                        ),
                    )
                },
                textStyle =
                    TextStyle(
                        fontSize = 8.sp,
                        textAlign = TextAlign.Start,
                    ),
                maxLines = 1,
                decorationBox = { innerTextField ->
                    Box(
                        modifier =
                            Modifier
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline,
                                    MaterialTheme.shapes.small,
                                ).padding(4.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        innerTextField()
                    }
                },
            )
            if (editableNames) {
                Box(
                    modifier =
                        Modifier
                            .height(24.dp)
                            .aspectRatio(1f)
                            .clickable {
                                isEditSubItem = !isEditSubItem
                            },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Outlined.Edit,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = null,
                        modifier =
                            Modifier
                                .size(12.dp)
                                .aspectRatio(1f),
                    )
                }
            }
        } else {
            Text(
                text = answer.groupName ?: "",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (editableNames) {
                Box(
                    modifier =
                        Modifier
                            .height(24.dp)
                            .aspectRatio(1f)
                            .clickable {
                                isEditSubItem = !isEditSubItem
                            },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Outlined.Edit,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = null,
                        modifier =
                            Modifier
                                .size(12.dp)
                                .aspectRatio(1f),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            var isErrorPeriod by remember { mutableStateOf(false) }
            var errorMessage by remember { mutableStateOf("") }

            BasicTextField(
                modifier =
                    Modifier
                        .height(28.dp)
                        .width(58.dp),
                value = answer.timePeriod?.inWholeSeconds.toString(),
                onValueChange = { newTime ->
                    try {
                        isErrorPeriod = false
                        errorMessage = ""

                        val updateTime =
                            if (newTime.isBlank()) {
                                Duration.ZERO
                            } else {
                                Duration.parse(
                                    newTime,
                                )
                            }

                        onUpdateAnswer(
                            FormularyAnswerCreate(
                                questionId = answer.questionId,
                                groupName = answer.groupName,
                                uid = answer.uid,
                                value = answer.value,
                                unit = answer.unit,
                                timePeriod = updateTime,
                                formId = answer.formId,
                            ),
                        )
                    } catch (e: Exception) {
                        isErrorPeriod = true
                        errorMessage = "Enter a valid period"
                    }
                },
                textStyle =
                    TextStyle(
                        fontSize = 8.sp,
                        textAlign = TextAlign.Start,
                    ),
                maxLines = 1,
                decorationBox = { innerTextField ->
                    Box(
                        modifier =
                            Modifier
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline,
                                    MaterialTheme.shapes.small,
                                ).padding(4.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        innerTextField()
                    }
                },
            )

            Spacer(modifier = Modifier.width(10.dp))

            BasicTextField(
                value = answer.value.toString(),
                modifier =
                    Modifier
                        .height(28.dp)
                        .width(30.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { newValue ->
                    val updatedValue = newValue.toFloatOrNull() ?: 0f

                    onUpdateAnswer(
                        FormularyAnswerCreate(
                            questionId = answer.questionId,
                            groupName = answer.groupName,
                            uid = answer.uid,
                            value = updatedValue,
                            unit = answer.unit,
                            timePeriod = answer.timePeriod,
                            formId = answer.formId,
                        ),
                    )
                },
                textStyle =
                    TextStyle(
                        fontSize = 8.sp,
                        textAlign = TextAlign.Start,
                    ),
                maxLines = 1,
                decorationBox = { innerTextField ->
                    Box(
                        modifier =
                            Modifier
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline,
                                    MaterialTheme.shapes.small,
                                ).padding(4.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        innerTextField()
                    }
                },
            )
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
@Preview
fun MultiFieldQuestionCardPreview(multiFieldViewModel: MultiFieldViewModel = MultiFieldViewModel()) {
    val question by multiFieldViewModel.question.collectAsState()

    val answer by multiFieldViewModel.answers.collectAsState()

    AppTheme {
        MultiFieldQuestionCard(
            question,
            true,
            onAnswerAdded = { answer ->
                multiFieldViewModel.onAnswerAdded(answer)

                Log.i("external", "${answer.value}")
            },
            onUpdateAnswer = { answer ->
                multiFieldViewModel.onAnswerChanged(answer)
            },
            onQuestionNameChanged = { name ->
                multiFieldViewModel.onNameChanged(name)
            },
        )
    }
}

class MultiFieldViewModel : ViewModel() {
    private val _question =
        MutableStateFlow(
            Question.MultiItem(
                id = 1,
                name = "",
                text = "",
                groupName = "",
                alternatives = mutableListOf(),
                dependencies = emptyList(),
            ),
        )

    private val currentId = MutableStateFlow(0)

    val question = _question.asStateFlow()

    private val _answers = MutableStateFlow(listOf<FormularyAnswerCreate>())

    val answers = _answers.asStateFlow()

    fun onNameChanged(value: String) {
        _question.value = question.value.copy(name = value)
    }

    fun onAnswerChanged(value: FormularyAnswerCreate) {
    }

    fun onAnswerAdded(answer: FormularyAnswerCreate) {
    }
}
