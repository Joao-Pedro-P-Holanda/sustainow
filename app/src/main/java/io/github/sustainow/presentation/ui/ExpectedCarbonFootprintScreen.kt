package io.github.sustainow.presentation.ui

import ConsumptionMainPage
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.sustainow.domain.model.Question
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.presentation.ui.components.MultiSelectQuestionCard
import io.github.sustainow.presentation.ui.components.NumericalSelectQuestionCard
import io.github.sustainow.presentation.ui.components.SingleSelectQuestionCard
import io.github.sustainow.presentation.ui.utils.getCurrentMonthNumber
import io.github.sustainow.presentation.viewmodel.FormularyViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpectedCarbonFootprintScreen(
    navController: NavController,
    viewModel: FormularyViewModel,
) {
    val formulary by viewModel.formulary.collectAsState()
    val currentQuestion by viewModel.currentQuestion.collectAsState()
    val totalValue by viewModel.totalValue.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val success by viewModel.success.collectAsState()
    val erro by viewModel.error.collectAsState()

    if (loading) {
        // Exibir indicador de carregamento enquanto os dados são carregados
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    } else if (success) {

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Card(
                modifier =
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    ),
                shape = RoundedCornerShape(8.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Resultado",
                        style = MaterialTheme.typography.headlineMedium, // Tamanho maior para o texto
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                    )
                    Text(
                        text = if(totalValue!= null)"${totalValue?.total} ${totalValue?.unit}" else "Erro ao calcular o consumo total",
                        style = MaterialTheme.typography.displayMedium, // Destaque maior para o valor
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                    )
                }
            }

            Button(
                onClick = { navController.navigate(ConsumptionMainPage) },
                modifier = Modifier.padding(top = 16.dp), // Espaçamento acima do botão
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
        val questions = formulary!!.questions
        val currentIndex = questions.indexOf(currentQuestion)
        val progress = if (questions.isNotEmpty()) (currentIndex + 1) / questions.size.toFloat() else 0f

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            // Indicador de progresso linear baseado no progresso das questões
            LinearProgressIndicator(
                progress = {
                    progress // Corrigido: deve ser um Float
                },
                modifier = Modifier.fillMaxWidth(),
            )

            // Exibir a questão atual
            currentQuestion?.let { question ->
                when (question) {
                    is Question.SingleSelect ->
                        SingleSelectQuestionCard(question) {
                                selectedAlternative ->
                            if (viewModel.userStateLogged is UserState.Logged) {
                                viewModel.addAnswerToQuestion(
                                    question = question,
                                    selectedAlternative = selectedAlternative,
                                    formId = formulary!!.id, // Certifique-se de passar os valores necessários
                                    uid = viewModel.userStateLogged.user.uid,
                                    groupName = "",
                                    month = getCurrentMonthNumber(),
                                )
                            }
                        }
                    is Question.MultiSelect ->
                        MultiSelectQuestionCard(question) {
                                selectedAlternative ->
                            if (viewModel.userStateLogged is UserState.Logged) {
                                viewModel.addAnswerToQuestion(
                                    question = question,
                                    selectedAlternative = selectedAlternative,
                                    formId = formulary!!.id, // Certifique-se de passar os valores necessários
                                    uid = viewModel.userStateLogged.user.uid,
                                    groupName = "",
                                    month = getCurrentMonthNumber(),
                                )
                            }
                        }
                    is Question.Numerical ->
                        NumericalSelectQuestionCard(question) {
                                selectedAlternative ->
                            if (viewModel.userStateLogged is UserState.Logged) {
                                viewModel.addAnswerToQuestion(
                                    question = question,
                                    selectedAlternative = selectedAlternative,
                                    formId = formulary!!.id, // Certifique-se de passar os valores necessários
                                    uid = viewModel.userStateLogged.user.uid,
                                    groupName = "",
                                    month = getCurrentMonthNumber(),
                                )
                            }
                        }
                    is Question.MultiItem -> {
                        Text("Question: ${question.text} (Multi Item)")
                    }
                }
            }

            // Botões de navegação
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = { viewModel.goToPreviousQuestion() },
                    enabled = currentIndex > 0, // Desabilitar se estiver na primeira questão
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        ),
                ) {
                    Text(text = "Retornar")
                }

                Button(
                    onClick = {
                        if (currentIndex == questions.size - 1) {
                            // Última questão, concluir o formulário
                            viewModel.sendAnswers()
                        } else {
                            // Avançar para a próxima questão
                            viewModel.goToNextQuestion()
                        }
                    },
                ) {
                    Text(if (currentIndex == questions.size - 1) "Concluir" else "Avançar")
                }
            }
        }
    }
}
