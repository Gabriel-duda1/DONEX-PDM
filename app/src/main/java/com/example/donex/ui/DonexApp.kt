package com.example.donex.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.donex.navigation.NavGraph
import com.example.donex.ui.theme.DonexTheme
import com.example.donex.viewmodel.AuthViewModel
import com.example.donex.viewmodel.ChatViewModel
import com.example.donex.viewmodel.MainViewModel
import com.example.donex.viewmodel.MapViewModel

@Composable
fun DonexApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val mainViewModel: MainViewModel = viewModel()
    val chatViewModel: ChatViewModel = viewModel()
    val mapViewModel: MapViewModel = viewModel()

    DonexTheme {
        NavGraph(
            navController = navController,
            authViewModel = authViewModel,
            mainViewModel = mainViewModel,
            chatViewModel = chatViewModel,
            mapViewModel = mapViewModel
        )
    }
}