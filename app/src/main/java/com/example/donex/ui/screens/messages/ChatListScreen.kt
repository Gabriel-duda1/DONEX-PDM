package com.example.donex.ui.screens.messages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.donex.ui.components.BottomNavBar
import com.example.donex.navigation.Screen
import com.example.donex.ui.components.BottomNavBar
import com.example.donex.viewmodel.ChatViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(viewModel: ChatViewModel, navController: NavController) {
    val chats by viewModel.myChats.collectAsState()
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    Scaffold(
        topBar = { TopAppBar(title = { Text("Minhas Conversas") }) },
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            items(chats) { chat ->
                val otherUserName = chat.userNames.entries.find { it.key != currentUserId }?.value ?: "Usuário"

                ListItem(
                    headlineContent = { Text(otherUserName, fontWeight = FontWeight.Bold) },
                    supportingContent = { Text(chat.lastMessage, maxLines = 1) },
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Chat.createRoute(chat.id))
                    }
                )
                Divider()
            }
        }
    }
}