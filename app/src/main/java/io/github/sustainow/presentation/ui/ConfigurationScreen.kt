package io.github.sustainow.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.sustainow.R
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.service.auth.AuthService
import kotlinx.coroutines.launch

@Composable
fun ConfigurationScreen(
    navController: NavController,
    userState: UserState,
    authService: AuthService,
    onChangeTheme: (Boolean) -> Unit
) {
    var isDarkTheme by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    // Estados para os campos de nome e sobrenome
    var firstName by remember {
        mutableStateOf(if (userState is UserState.Logged) userState.user.firstName else "Nome")
    }
    var lastName by remember {
        mutableStateOf(if (userState is UserState.Logged) userState.user.lastName else "Sobrenome")
    }

    var collectiveActionIsChecked by remember { mutableStateOf(true) }
    var routineIsChecked by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_picture),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
        )

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)) {
            Text(
                text = "Nome",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            BasicTextField(
                value = firstName,
                onValueChange = { firstName = it },
                singleLine = true,
                textStyle = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.LightGray.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(8.dp)
            )
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)) {
            Text(
                text = "Sobrenome",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            BasicTextField(
                value = lastName,
                onValueChange = { lastName = it },
                singleLine = true,
                textStyle = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.LightGray.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(8.dp)
            )
        }

        Divider(color = Color.LightGray, thickness = 1.dp)

        Text(text = "Informações Pessoais", style = MaterialTheme.typography.titleLarge)

        Button(onClick = { /* TODO : implementar logica pra mudar email */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Alterar Email")
        }

        Button(onClick = { /* TODO: implementar logica para mudar senha */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Alterar Senha")
        }

        Button(onClick = { /* TODO: implementar locia pra mudar pais*/ }, modifier = Modifier.fillMaxWidth()) {
            Text("País: Brasil")
        }

        Divider(color = Color.LightGray, thickness = 1.dp)


        Text(text = "Preferências", style = MaterialTheme.typography.titleLarge)
        Row {
            Text("Tema escuro", style = MaterialTheme.typography.bodyMedium)
            Switch(
                checked = isDarkTheme,
                onCheckedChange = {
                    isDarkTheme = it
                    onChangeTheme(it) // Chama a função de troca de tema
                },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.Green)
            )
        }


        Text(text = "Notificações", style = MaterialTheme.typography.titleLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Atualizações de ações coletivas")
            Switch(
                checked = collectiveActionIsChecked,
                onCheckedChange = { collectiveActionIsChecked = it }, // Atualiza o estado corretamente
                modifier = Modifier.padding(8.dp),
                colors = SwitchDefaults.colors(checkedThumbColor = Color.Green)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Rotina")
            Switch(
                checked = routineIsChecked,
                onCheckedChange = { routineIsChecked = it }, // Atualiza o estado corretamente
                modifier = Modifier.padding(8.dp),
                colors = SwitchDefaults.colors(checkedThumbColor = Color.Green)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Medidas",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            MeasurementField(label = "Pegada de Carbono", units = listOf("kg", "tonelada", "g"))
            MeasurementField(label = "Consumo de Energia", units = listOf("kWh", "MWh", "Joule"))
            MeasurementField(label = "Consumo de Água", units = listOf("m³", "litros", "galões"))
        }
        // Action Buttons
        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        authService.signOut()
                        navController.navigate("Login")
                        // Aqui você pode redirecionar o usuário, por exemplo:
                        // navigateToLoginScreen()
                    } catch (e: Exception) {
                        // Trate erros, como falha na operação de logout
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
            onClick = { /*todo : logica pra deletar conta*/},
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Excluir conta", color = Color.White)
        }
    }
}

@Composable
fun MeasurementField(label: String, units: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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


