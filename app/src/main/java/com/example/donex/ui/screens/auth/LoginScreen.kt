package com.example.donex.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.donex.viewmodel.AuthViewModel
import com.example.donex.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DonexCremeFundo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "DONEX",
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            color = DonexBrown
        )
        Text(
            text = "Conectando solidariedade",
            fontSize = 16.sp,
            color = DonexTextDark
        )

        Spacer(modifier = Modifier.height(48.dp))


        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DonexBrown,
                unfocusedBorderColor = DonexBrown.copy(alpha = 0.6f),
                focusedLabelColor = DonexBrown,
                cursorColor = DonexBrown
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DonexBrown,
                unfocusedBorderColor = DonexBrown.copy(alpha = 0.6f),
                focusedLabelColor = DonexBrown,
                cursorColor = DonexBrown
            )
        )

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.login(email, password, onLoginSuccess) },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            enabled = !loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = DonexBrown,
                contentColor = DonexWhite
            )
        ) {
            if (loading) {
                CircularProgressIndicator(color = DonexWhite, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
            } else {
                Text("ENTRAR", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text(
                text = "Não tem conta? Cadastre-se",
                color = DonexBrown,
                fontWeight = FontWeight.Bold
            )
        }
    }
}