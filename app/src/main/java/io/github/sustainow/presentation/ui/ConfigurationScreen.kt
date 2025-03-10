package io.github.sustainow.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.sustainow.R
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.presentation.ui.utils.ThemeOption
import io.github.sustainow.presentation.viewmodel.ThemeViewModel
import io.github.sustainow.presentation.viewmodel.ThemeViewModelFactory
import io.github.sustainow.service.auth.AuthService
import kotlinx.coroutines.launch

@Composable
fun ConfigurationScreen(
    navController: NavController,
    userState: UserState,
    authService: AuthService,
    onChangeTheme: (Boolean) -> Unit,
    themeViewModel: ThemeViewModel = viewModel(factory = ThemeViewModelFactory(LocalContext.current))
) {
    // Observar se o tema é escuro ou claro
    val isDarkTheme = themeViewModel.isDarkTheme.value
    val coroutineScope = rememberCoroutineScope()

    // Estados para os campos de nome e sobrenome
    var firstName by remember { mutableStateOf(if (userState is UserState.Logged) userState.user.firstName else "Nome") }
    var lastName by remember { mutableStateOf(if (userState is UserState.Logged) userState.user.lastName else "Sobrenome") }

    var collectiveActionIsChecked by remember { mutableStateOf(true) }
    var routineIsChecked by remember { mutableStateOf(true) }

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(themeViewModel.themeOption.value) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagem de perfil
        Image(
            painter = painterResource(id = R.drawable.profile_picture),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
        )

        // Campo para o primeiro nome
        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Text(
                text = "Nome",
                style = TextStyle(fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            BasicTextField(
                value = firstName,
                onValueChange = { firstName = it },
                singleLine = true,
                textStyle = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.LightGray.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp))
                    .padding(8.dp)
            )
        }

        // Campo para o sobrenome
        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Text(
                text = "Sobrenome",
                style = TextStyle(fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            BasicTextField(
                value = lastName,
                onValueChange = { lastName = it },
                singleLine = true,
                textStyle = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.LightGray.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp))
                    .padding(8.dp)
            )
        }

        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

        Text(text = "Informações Pessoais", style = MaterialTheme.typography.titleLarge)

        Button(onClick = { /* TODO: lógica para mudar email */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Alterar Email")
        }

        Button(onClick = { /* TODO: lógica para mudar senha */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Alterar Senha")
        }

        Button(onClick = { /* TODO: lógica para mudar país */ }, modifier = Modifier.fillMaxWidth()) {
            Text("País: Brasil")
        }

        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

        Text(text = "Preferências", style = MaterialTheme.typography.titleLarge)

        // Switch para mudar o tema
        Column {
            Text("Mudar tema", style = MaterialTheme.typography.bodyMedium)
            Box {
                TextButton(onClick = { expanded = true }) {
                    Text(text = selectedOption.name)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    ThemeOption.values().forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.name) },
                            onClick = {
                                selectedOption = option
                                expanded = false
                                themeViewModel.updateTheme(option)
                            }
                        )
                    }
                }
            }
        }

        Text(text = "Notificações", style = MaterialTheme.typography.titleLarge)

        // Switch para "Ações Coletivas"
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Atualizações de ações coletivas")
            Switch(
                checked = collectiveActionIsChecked,
                onCheckedChange = { collectiveActionIsChecked = it },
                modifier = Modifier.padding(8.dp),
                colors = SwitchDefaults.colors(checkedThumbColor = Color.Green)
            )
        }

        // Switch para "Rotina"
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Rotina")
            Switch(
                checked = routineIsChecked,
                onCheckedChange = { routineIsChecked = it },
                modifier = Modifier.padding(8.dp),
                colors = SwitchDefaults.colors(checkedThumbColor = Color.Green)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Medidas
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = "Medidas",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            MeasurementField(label = "Pegada de Carbono", units = listOf("kg", "tonelada", "g"))
            MeasurementField(label = "Consumo de Energia", units = listOf("kWh", "MWh", "Joule"))
            MeasurementField(label = "Consumo de Água", units = listOf("m³", "litros", "galões"))
        }

        // Botões de ação
        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        authService.signOut()
                        navController.navigate("Login")
                    } catch (e: Exception) {
                        println("Erro ao fazer logout: ${e.message}")
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sair da conta", color = Color.White)
        }

        Button(
            onClick = { /* TODO: lógica para deletar conta */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Excluir conta", color = Color.White)
        }
    }
}

// Função para os campos de medida
@Composable
fun MeasurementField(label: String, units: List<String>) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        MeasurementOption(units = units)
    }
}

// Função para selecionar a unidade de medida
@Composable
fun MeasurementOption(units: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(units.first()) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Unidade:", style = MaterialTheme.typography.bodyMedium)
        Box {
            TextButton(onClick = { expanded = true }) {
                Text(text = selectedOption)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                units.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit) },
                        onClick = {
                            selectedOption = unit
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
