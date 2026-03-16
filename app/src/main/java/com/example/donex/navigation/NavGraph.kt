package com.example.donex.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.donex.ui.screens.auth.LoginScreen
import com.example.donex.ui.screens.auth.RegisterScreen
import com.example.donex.ui.screens.feed.FeedScreen
import com.example.donex.ui.screens.map.MapScreen
import com.example.donex.ui.screens.messages.ChatListScreen
import com.example.donex.ui.screens.messages.ChatScreen
import com.example.donex.ui.screens.profile.ProfileScreen
import com.example.donex.viewmodel.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel,
    chatViewModel: ChatViewModel,
    mapViewModel: MapViewModel
) {
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) Screen.Feed.route else Screen.Login.route

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) { LoginScreen(authViewModel, { navController.navigate(Screen.Register.route) }, { navController.navigate(Screen.Feed.route) { popUpTo(Screen.Login.route) { inclusive = true } } }) }
        composable(Screen.Register.route) { RegisterScreen(authViewModel, { navController.popBackStack() }, { navController.navigate(Screen.Feed.route) { popUpTo(Screen.Login.route) { inclusive = true } } }) }
        composable(Screen.Feed.route) { FeedScreen(mainViewModel, navController) }
        composable(Screen.Map.route) { MapScreen(mainViewModel, mapViewModel, navController) }
        composable(Screen.Messages.route) { ChatListScreen(chatViewModel, navController) }

        composable(
            route = Screen.Chat.route,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ChatScreen(chatId, chatViewModel, onNavigateBack = { navController.popBackStack() })
        }

        // Rota de Perfil com ID opcional
        composable(
            route = "profile/{userId}",
            arguments = listOf(navArgument("userId") { defaultValue = FirebaseAuth.getInstance().currentUser?.uid ?: "" })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ProfileScreen(
                targetUserId = userId,
                authViewModel = authViewModel,
                mainViewModel = mainViewModel,
                navController = navController,
                onLogout = {
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                }
            )
        }
    }
}