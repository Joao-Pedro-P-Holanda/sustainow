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
import io.github.sustainow.R

@Composable
fun ConfigurationScreen(modifier: Modifier = Modifier) {
    var firstName by remember { mutableStateOf("User") }
    var lastName by remember { mutableStateOf("") }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var routineEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
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
        Button(onClick = { /* TODO: implementar locia pra tema*/ }, modifier = Modifier.fillMaxWidth()) {
            Text("Tema")
        }


        Text(text = "Notificações", style = MaterialTheme.typography.titleLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Atualizações de ações coletivas")
            Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Rotina")
            Switch(checked = routineEnabled, onCheckedChange = { routineEnabled = it })
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        Button(
            onClick = { /* Todo: logica pra fazer logout pela tela de configuração */ },
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
