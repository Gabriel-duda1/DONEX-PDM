package com.example.donex.ui.screens.messages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.donex.ui.components.BottomNavBar
import com.example.donex.viewmodel.ChatViewModel
import com.example.donex.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(viewModel: ChatViewModel, navController: NavController) {
    val chats by viewModel.myChats.collectAsState()
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Minhas Conversas", color = DonexBrown, fontWeight = FontWeight.Bold)
            })
        },
        bottomBar = { BottomNavBar(navController) },
        containerColor = DonexCremeFundo
    ) { padding ->
        if (chats.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("Nenhuma conversa iniciada.", color = DonexTextDark)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
                items(chats) { chat ->
                    val otherUserId = chat.userIds.firstOrNull { it != currentUserId }
                    var displayName by remember {
                        mutableStateOf(chat.userNames.filterKeys { it != currentUserId }.values.firstOrNull() ?: "Carregando...")
                    }

                    if (displayName == "Usuário" || displayName == "Carregando..." || displayName == "Meu Usuário") {
                        LaunchedEffect(otherUserId) {
                            if (otherUserId != null) {
                                FirebaseFirestore.getInstance().collection("users").document(otherUserId).get()
                                    .addOnSuccessListener { doc ->
                                        val nomeNoBanco = doc.getString("name")
                                        if (!nomeNoBanco.isNullOrEmpty()) {
                                            displayName = nomeNoBanco
                                        }
                                    }
                            }
                        }
                    }

                    ListItem(
                        headlineContent = { Text(displayName, fontWeight = FontWeight.Bold, color = DonexTextDark) },
                        supportingContent = { Text(chat.lastMessage, maxLines = 1, color = Color.Gray) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier.clickable { navController.navigate("chat/${chat.id}") }
                    )
                    Divider(
                        color = DonexBrown.copy(alpha = 0.1f),
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}