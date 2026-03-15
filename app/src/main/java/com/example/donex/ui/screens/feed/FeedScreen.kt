package com.example.donex.ui.screens.feed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.donex.model.Post
import com.example.donex.navigation.Screen
import com.example.donex.ui.components.BottomNavBar
import com.example.donex.ui.components.DonationInput
import com.example.donex.ui.components.PostCard
import com.example.donex.viewmodel.MainViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(viewModel: MainViewModel, navController: NavController) {
    val posts by viewModel.posts.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val auth = Firebase.auth

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text("DONEX", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.headlineMedium)
                Divider()

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
                    label = { Text("Sair do Aplicativo") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            auth.signOut()
                            drawerState.close()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0)
                            }
                        }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedTextColor = Color.Red,
                        unselectedIconColor = Color.Red
                    )
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("DONEX Feed") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = { BottomNavBar(navController) }
        ) { padding ->
            Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                DonationInput(onPostClick = { texto, tipo ->
                    val newPost = Post(conteudo = texto, tipo = tipo)
                    viewModel.createPost(newPost) { /* sucesso */ }
                })

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(posts) { post ->
                        PostCard(post = post, onChatClick = {

                        })
                    }
                }
            }
        }
    }
}