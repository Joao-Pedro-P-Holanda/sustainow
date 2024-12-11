package io.github.sustainow.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.sustainow.domain.model.FormularyAnswer
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.presentation.ui.components.LoadingModal
import io.github.sustainow.presentation.viewmodel.HomeViewModel
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days

/*
* HomeScreen is the main screen of the application.
* TODO: remove logout button when the scope is finished
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    userState: UserState,
    redirectLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val questions = viewModel.displayQuestions.collectAsState()
    val formulary = viewModel.displayFormulary.collectAsState()
    val answers by viewModel.displayAnswers.collectAsState()
    when (userState) {
        is UserState.NotLogged -> {
            redirectLogin()
        }
        is UserState.Loading -> {
            LoadingModal()
        }
        is UserState.Logged -> {
            Column(
                modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Button(onClick = { viewModel.signOut() }) {
                    Text(text = "Logout")
                }
                Button(onClick = { viewModel.getFormulary("carbon_footprint") }) {
                    Text(text = "Get Formulary")
                }
                Button(onClick = { viewModel.getQuestions("carbon_footprint") }) {
                    Text(text = "Get Questions")
                }
                Button(onClick = {
                    val testAnswers =
                        listOf(
                            FormularyAnswer(
                                formId = 2,
                                uid = userState.user.uid,
                                value = 1.0f,
                                unit = "kg",
                                groupName = "test_group",
                                questionId = 1,
                                month = 1,
                                timePeriod = 30.days,
                                answerDate = Instant.parse("2021-10-10T00:00:00+00"),
                            ),
                        ) // Replace with actual answers
                    viewModel.addAnswers(testAnswers)
                }) {
                    Text(text = "Add Answers")
                }
                Button(onClick = { viewModel.getAnswers("carbon_footprint") }) {
                    Text(text = "Get Answers")
                }
                // Display the current state of formulary
                formulary.value.let {
                    Text(text = "Formulary:")
                    if (it != null) {
                        Text(text = "Area: ${it.area}")
                    }
                    if (it != null) {
                        Text(text = "ID: ${it.id}")
                    }
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                } ?: Text(text = "No Formulary")

                // Display the current state of questions
                Text(text = "Questions: ${questions.value?.size ?: 0}")
                questions.value?.forEach { question ->
                    Text(text = "Question:")
                    Text(text = "Name: ${question.name}")
                    Text(text = "Text: ${question.text}")
                    Text(text = "Alternatives: ${question.alternatives.size}")
                    Text(text = "Dependencies: ${question.dependencies.size}")
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }

                // Display the current state of answers
                Text(text = "Answers: ${answers?.size ?: 0}")
                answers?.forEach { answer ->
                    Text(text = "Answer:")
                    Text(text = "ID: ${answer.id}")
                    Text(text = "Form ID: ${answer.formId}")
                    Text(text = "User ID: ${answer.uid}")
                    Text(text = "Value: ${answer.value}")
                    Text(text = "Unit: ${answer.unit}")
                    Text(text = "Group Name: ${answer.groupName}")
                    Text(text = "Question: ${answer.questionId}")
                    Text(text = "Month: ${answer.month}")
                    Text(text = "Answer Date: ${answer.answerDate}")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }
}
