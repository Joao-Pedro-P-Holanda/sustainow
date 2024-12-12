import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.sustainow.presentation.viewmodel.FormularyViewModel

@Composable
fun PreviewFormulary(viewModel: FormularyViewModel) {
    val currentQuestion by viewModel.currentQuestion.collectAsState()
    val formulary by viewModel.formulary.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        formulary?.let { form ->
            Text(text = "Formulary Area: ${form.area}")
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                form.questions.forEach { question ->
                    Text(text = "Question: ${question.text}")
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        } ?: Text(text = "No Formulary")

        Spacer(modifier = Modifier.height(16.dp))

        currentQuestion?.let { question ->
            Text(text = question.text)
        } ?: Text(text = "No current question")

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(onClick = { viewModel.goToPreviousQuestion() }) {
                Text(text = "Previous")
            }
            Button(onClick = { viewModel.goToNextQuestion() }) {
                Text(text = "Next")
            }
        }
    }
}
