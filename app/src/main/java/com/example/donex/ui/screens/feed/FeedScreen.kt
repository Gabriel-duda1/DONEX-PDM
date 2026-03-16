package com.example.donex.ui.screens.feed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.donex.model.Post
import com.example.donex.navigation.Screen
import com.example.donex.ui.components.AppDrawer
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
        gesturesEnabled = false,
        drawerContent = {
            AppDrawer(
                onProfileClick = {
                    scope.launch {
                        drawerState.close()
                        val currentUid = auth.currentUser?.uid ?: ""
                        navController.navigate("profile/$currentUid")
                    }
                },
                onChatListClick = {
                    scope.launch {
                        drawerState.close()
                        navController.navigate("chat_list")
                    }
                },
                onLogoutClick = {
                    scope.launch {
                        auth.signOut()
                        drawerState.close()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0)
                        }
                    }
                },
                onCloseClick = {
                    scope.launch { drawerState.close() }
                }
            )
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
                DonationInput(onPostClick = { texto, tipo, url ->
                    val newPost = Post(conteudo = texto, tipo = tipo)
                    viewModel.createPost(newPost, url) { }
                })

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(posts) { post ->
                        PostCard(
                            post = post,
                            onChatClick = {
                                val currentUserId = auth.currentUser?.uid
                                if (currentUserId != null && post.userId != currentUserId) {
                                    viewModel.iniciarConversa(currentUserId, post.userId) { chatId ->
                                        navController.navigate("chat/$chatId")
                                    }
                                }
                            },
                            onDeleteClick = {
                                viewModel.deletePost(post.id)
                            },
                            onUserClick = {
                                navController.navigate("profile/${post.userId}")
                            }
                        )
                    }
                }
            }
        }
    }
}