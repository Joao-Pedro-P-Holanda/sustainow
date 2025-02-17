package io.github.sustainow.presentation.ui

import ConsumptionMainPage
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.sustainow.domain.model.Question
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.presentation.ui.components.MultiSelectQuestionCard
import io.github.sustainow.presentation.ui.components.NumericalSelectQuestionCard
import io.github.sustainow.presentation.ui.components.SingleSelectQuestionCard
import io.github.sustainow.presentation.ui.components.formulary.ReuseAnswersDialog
import io.github.sustainow.presentation.viewmodel.FormularyViewModel

@Composable
fun ExpectedCarbonFootprintScreen(
    navController: NavController,
    viewModel: FormularyViewModel,
) {
    val formulary by viewModel.formulary.collectAsState()
    val currentQuestion by viewModel.currentQuestion.collectAsState()
    val totalValue by viewModel.totalValue.collectAsState()
    val selectedAnswers = viewModel.selectedAnswers
    val loading by viewModel.loading.collectAsState()
    val success by viewModel.success.collectAsState()
    val erro by viewModel.error.collectAsState()

    val showReuseAnswersDialog by viewModel.showReuseAnswersDialog.collectAsState()


    if (loading) {
        // Exibir indicador de carregamento enquanto os dados são carregados
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    } else if (success) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(0.8f) // Reduzindo a largura
                        .height(250.dp) // Reduzindo a altura
                        .clip(RoundedCornerShape(16.dp)) // Arredondamento maior
                        .background(Color(0xff18153f))
                        .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    Text(
                        text = "Resultado",
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.White,
                    )
                    Box(
                        modifier =
                            Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .border(width = 3.dp, color = Color.Green, shape = CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = totalValue?.let { "${it.total} ${it.unit}" } ?: "Erro ao calcular",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
            }

            Button(
                onClick = { navController.navigate(ConsumptionMainPage) },
                modifier = Modifier.padding(top = 16.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
            ) {
                Text("Voltar")
            }
        }
    } else if (erro != null) {
        if (erro!!.source === formulary) {
            Card(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onSurface,
                    ),
                shape = RoundedCornerShape(8.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Erro ao carregar formulário",
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                    )
                    Button(
                        onClick = { navController.navigate(ConsumptionMainPage) },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                            ),
                    ) {
                        Text(text = "Voltar")
                    }
                }
            }
        } else {
            Card(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onSurface,
                    ),
                shape = RoundedCornerShape(8.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Erro ao carregar perguntas",
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                    )
                    Button(
                        onClick = { navController.navigate(ConsumptionMainPage) },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                            ),
                    ) {
                        Text(text = "Voltar")
                    }
                }
            }
        }
    } else {
        val questions = formulary?.questions
        val currentIndex = questions?.indexOf(currentQuestion)
        val progress = if (questions?.isNotEmpty() == true) (currentIndex?.plus(1))?.div(questions.size.toFloat()) else 0f

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            ReuseAnswersDialog(
                showReuseAnswersDialog,
                onDismissRequest = {viewModel.hideReuseAnswersDialog()},
                onAcceptRequest = {viewModel.reuseCurrentAnswers()}
            )


            // Indicador de progresso linear baseado no progresso das questões
            LinearProgressIndicator(
                progress = {
                    progress ?: 0f // Corrigido: deve ser um Float
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Text(currentQuestion?.groupName ?: "", style = MaterialTheme.typography.displaySmall, textAlign = TextAlign.Center)

            // Exibir a questão atual
            currentQuestion?.let { question ->
                when (question) {
                    is Question.SingleSelect ->
                        SingleSelectQuestionCard(
                            question,
                            onAnswerAdded = { selectedAlternative ->
                                if (viewModel.userStateLogged is UserState.Logged) {
                                    viewModel.addAnswerToQuestion(question, selectedAlternative)
                                }
                            },
                            onAnswerRemoved = { selectedAlternative ->
                                if (viewModel.userStateLogged is UserState.Logged) {
                                    viewModel.onAnswerRemoved(question, selectedAlternative)
                                }
                            },
                            selectedAnswers = selectedAnswers[question] ?: emptyList(),
                        )
                    is Question.MultiSelect ->
                        MultiSelectQuestionCard(
                            question,
                            onAnswerAdded = { selectedAlternative ->
                                if (viewModel.userStateLogged is UserState.Logged) {
                                    viewModel.addAnswerToQuestion(question, selectedAlternative)
                                }
                            },
                            onAnswerRemoved = { selectedAlternative ->
                                if (viewModel.userStateLogged is UserState.Logged) {
                                    viewModel.onAnswerRemoved(question, selectedAlternative)
                                }
                            },
                            selectedAnswers = selectedAnswers[question] ?: emptyList(),
                        )
                    is Question.Numerical ->
                        NumericalSelectQuestionCard(
                            question,
                            onAnswerAdded = { selectedAlternative ->
                                if (viewModel.userStateLogged is UserState.Logged) {
                                    viewModel.addAnswerToQuestion(question, selectedAlternative)
                                }
                            },
                            onAnswerRemoved = { selectedAlternative ->
                                if (viewModel.userStateLogged is UserState.Logged) {
                                    viewModel.onAnswerRemoved(question, selectedAlternative)
                                }
                            },
                            selectedAnswers = selectedAnswers[question] ?: emptyList(),
                        )
                    is Question.MultiItem -> {
                    }
                }
            }

            // Botões de navegação
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (currentIndex != null) {
                    Button(
                        onClick = { viewModel.goToPreviousQuestion() },
                        enabled = currentIndex > 0, // Desabilitar se estiver na primeira questão
                        colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary,
                        ),
                    ) {
                        Text(text = "Retornar")
                    }
                }

                Button(
                    onClick = {
                        if (questions != null) {
                            if (currentIndex == questions.size - 1) {
                                // Última questão, concluir o formulário
                                viewModel.sendAnswers()
                            } else {
                                // Avançar para a próxima questão
                                viewModel.goToNextQuestion()
                            }
                        }
                    },
                ) {
                    if (questions != null) {
                        Text(if (currentIndex == questions.size - 1) "Concluir" else "Avançar")
                    }
                }
            }
        }
    }
}
