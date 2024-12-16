package io.github.sustainow.presentation.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.sustainow.R
import io.github.sustainow.domain.model.Question
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.presentation.ui.components.NumericalSelectQuestionCard
import io.github.sustainow.presentation.ui.utils.getCurrentMonthAbbreviated
import io.github.sustainow.presentation.ui.utils.getCurrentMonthNumber
import io.github.sustainow.presentation.ui.utils.getCurrentYear
import io.github.sustainow.presentation.viewmodel.FormularyViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealEnergyConsumptionScreen(
    modifier: Modifier = Modifier,
    defaultErrorAction: () -> Unit,
    viewModel: FormularyViewModel,
) {
    val formulary by viewModel.formulary.collectAsState()
    val previousAnswers by viewModel.previousAnswers.collectAsState()
    val success by viewModel.success.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        when (loading) {
            true -> {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
                }
            }
            else -> {
                when {
                    // must come first to avoid false positive with null answers or formularies, as the === operator matches
                    // with any object having null, and answers can be nullable
                    error?.source == null -> {
                        if (success) {
                            val totalValue = viewModel.calculateTotalValue()
                            Column(
                                modifier =
                                    modifier.fillMaxWidth().height(
                                        300.dp,
                                    ).clip(RoundedCornerShape(8.dp)).background(Color(0xff18153f)).padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(32.dp),
                            ) {
                                Text(
                                    stringResource(id = R.string.result),
                                    style = MaterialTheme.typography.displaySmall,
                                    color = Color.White,
                                )
                                Box(
                                    modifier =
                                        modifier.size(
                                            160.dp,
                                        ).clip(CircleShape).border(width = 3.dp, color = Color.Green, shape = CircleShape),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text("$totalValue R$", color = Color.White, style = MaterialTheme.typography.titleLarge)
                                }
                            }
                            Button(onClick = { defaultErrorAction() }) {
                                Text(stringResource(R.string.back))
                            }
                        } else {
                            Text(
                                stringResource(id = R.string.real_energy_consumption_title),
                                style = MaterialTheme.typography.headlineSmall,
                            )
                            Text(stringResource(id = R.string.real_energy_consumption_message))

                            Card(
                                modifier = modifier.fillMaxWidth(),
                                colors =
                                    CardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceTint,
                                        contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                                        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                        disabledContentColor = MaterialTheme.colorScheme.onSurface,
                                    ),
                            ) {
                                Row(
                                    modifier =
                                        modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    if (previousAnswers.isNotEmpty()) {
                                        Column(verticalArrangement = Arrangement.SpaceBetween) {
                                            Text(
                                                stringResource(R.string.previous_real_consumption_title),
                                                style = MaterialTheme.typography.titleLarge,
                                            )
                                            // sum of the answers made in the previous month
                                            Text("${previousAnswers.fold(0f) { acc, answer -> acc + answer.value }} R$")
                                        }
                                        Column {
                                            AssistChip(
                                                onClick = { },
                                                label = {
                                                    Text(
                                                        "${getCurrentMonthAbbreviated()}  ${getCurrentYear()}",
                                                    )
                                                },
                                                colors =
                                                    AssistChipDefaults.assistChipColors(
                                                        disabledLabelColor = MaterialTheme.colorScheme.inverseOnSurface,
                                                    ),
                                                enabled = false,
                                            )
                                            // sum of the answers made in the previous month
                                            Text("${previousAnswers.fold(0f) { acc, answer -> acc + answer.value }} R$")
                                        }
                                    } else {
                                        Text(
                                            stringResource(id = R.string.no_previous_real_consumption_message),
                                        )
                                    }
                                }
                            }

                            // renders all numerical question in a row with wrap layout
                            for (question in formulary?.questions!!) {
                                when (question) {
                                    is Question.Numerical ->
                                        NumericalSelectQuestionCard(question) { selectedAlternative ->
                                            if (viewModel.userStateLogged is UserState.Logged) {
                                                viewModel.addAnswerToQuestion(
                                                    question = question,
                                                    selectedAlternative = selectedAlternative,
                                                    formId = formulary!!.id,
                                                    uid = viewModel.userStateLogged.user.uid,
                                                    groupName = null,
                                                    month = getCurrentMonthNumber(),
                                                )
                                            }
                                        }
                                    // renders multifield questions below other questions
                                    else -> {
                                        TODO("Multifield rendering")
                                    }
                                }
                            }
                            Button(onClick = { viewModel.sendAnswers() }) {
                                Text(stringResource(id = R.string.submit_real_energy_consumption_message))
                            }
                        }
                    }
                    error?.source === "formulary" -> {
                        BasicAlertDialog(onDismissRequest = { defaultErrorAction() }) {
                            Card(modifier = modifier.requiredSize(200.dp, 160.dp)) {
                                Column(
                                    modifier =
                                        Modifier
                                            .fillMaxSize()
                                            .padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Error,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                    )
                                    Text(text = stringResource(id = R.string.formulary_fetch_error_message), textAlign = TextAlign.Center)
                                    Button(
                                        onClick = { viewModel.formularyFetch() },
                                    ) {
                                        Text(text = stringResource(id = R.string.retry_load_message))
                                    }
                                }
                            }
                        }
                    }
                    error?.source === "answers" -> {
                        BasicAlertDialog(onDismissRequest = { defaultErrorAction() }) {
                            Card(modifier = modifier.requiredSize(200.dp, 160.dp)) {
                                Column(
                                    modifier =
                                        Modifier
                                            .fillMaxSize()
                                            .padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Error,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                    )
                                    Text(
                                        text = stringResource(id = R.string.failed_create_answers_error_message),
                                        textAlign = TextAlign.Center,
                                    )
                                    Button(
                                        onClick = { viewModel.formularyFetch() },
                                    ) {
                                        Text(text = stringResource(id = R.string.retry_load_message))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
