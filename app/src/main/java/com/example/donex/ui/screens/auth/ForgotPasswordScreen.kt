package com.example.donex.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ForgotPasswordScreen(onNavigateBack: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Recuperar Senha", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Enviaremos um link para o seu e-mail.")

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Seu E-mail") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { message = if(it.isSuccessful) "E-mail enviado!" else "Erro ao enviar." }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("ENVIAR LINK")
        }

        message?.let { Text(it, modifier = Modifier.padding(top = 8.dp)) }

        TextButton(onClick = onNavigateBack) { Text("Voltar") }
    }
}