package com.example.donex.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Login : Screen("login", "Login")
    object Register : Screen("register", "Cadastro")
    object ForgotPassword : Screen("forgot_password", "Recuperar Senha")
    object Feed : Screen("feed", "Feed", Icons.Default.Home)
    object Map : Screen("map", "Mapa", Icons.Default.LocationOn)
    object Messages : Screen("messages", "Mensagens", Icons.Default.Email)
    object Profile : Screen("profile", "Perfil", Icons.Default.Person)
    object Chat : Screen("chat/{chatId}", "Conversa") {
        fun createRoute(chatId: String) = "chat/$chatId"
    }
}