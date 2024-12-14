package io.github.sustainow.presentation.ui

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.sustainow.domain.model.Question
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import io.github.sustainow.presentation.viewmodel.FormularyViewModel
import io.github.sustainow.presentation.ui.components.SingleSelectQuestionCard
import io.github.sustainow.presentation.ui.components.MultiSelectQuestionCard
import io.github.sustainow.presentation.ui.components.NumericalSelectQuestionCard

@Composable
fun LinearDeterminateIndicator() {
    var currentProgress by remember { mutableStateOf(0f) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope() // Create a coroutine scope

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = {
            loading = true
            scope.launch {
                loadProgress { progress ->
                    currentProgress = progress
                }
                loading = false // Reset loading when the coroutine finishes
            }
        }, enabled = !loading) {
            Text("Start loading")
        }

        if (loading) {
            LinearProgressIndicator(
                progress = { currentProgress },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/** Iterate the progress value */
suspend fun loadProgress(updateProgress: (Float) -> Unit) {
    for (i in 1..100) {
        updateProgress(i.toFloat() / 100)
        delay(100)
    }
}

@Composable
fun ExpectedCarbonFootprintScreen(
    navController: NavController,
    viewModel: FormularyViewModel,
) {
    val formulary by viewModel.formulary.collectAsState(initial = null)
    val currentQuestion by viewModel.currentQuestion.collectAsState(initial = null)
    val loading by viewModel.loading.collectAsState(initial = false)
    val success by viewModel.success.collectAsState(initial = false)

    if (loading) {
        // Exibir indicador de carregamento enquanto os dados são carregados
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    } else if (success) {
        // Exibir mensagem de sucesso ao concluir o formulário
        Text("Formulário concluído com sucesso!")
    } else if (formulary == null) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onSurface
                ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Erro ao carregar o formulário.",
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Por favor, tente novamente ou volte à tela anterior.",
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text("Voltar")
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
            modifier = Modifier.fillMaxWidth()
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
                    is Question.SingleSelect -> SingleSelectQuestionCard(question) { }
                    is Question.MultiSelect -> MultiSelectQuestionCard(question) { }
                    is Question.Numerical -> NumericalSelectQuestionCard(question) { }
                    is Question.MultiItem -> {
                        Text("Question: ${question.text} (Multi Item)")
                    }
                }
            }

            // Botões de navegação
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { viewModel.goToPreviousQuestion() },
                    enabled = currentIndex > 0, // Desabilitar se estiver na primeira questão
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text("Retornar")
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
                    }
                ) {
                    Text(if (currentIndex == questions.size - 1) "Concluir" else "Avançar")
                }
            }
        }
    }
}
