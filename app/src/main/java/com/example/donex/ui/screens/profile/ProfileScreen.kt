package com.example.donex.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.donex.ui.components.PostCard
import com.example.donex.viewmodel.MainViewModel
import com.example.donex.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.donex.ui.theme.DonexBrown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    targetUserId: String,
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel,
    navController: NavController,
    onLogout: () -> Unit
) {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    val isMyProfile = targetUserId == currentUserId
    val posts by mainViewModel.posts.collectAsState()
    val userPosts = posts.filter { it.userId == targetUserId }
    val bioSaved by authViewModel.bioSaved.collectAsState()
    val context = LocalContext.current

    var nomeUsuario by remember { mutableStateOf("Carregando...") }
    var emailUsuario by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    LaunchedEffect(targetUserId) {
        FirebaseFirestore.getInstance().collection("users").document(targetUserId).get()
            .addOnSuccessListener { doc ->
                nomeUsuario = doc.getString("name") ?: "Usuário"
                emailUsuario = doc.getString("email") ?: ""
                descricao = doc.getString("descricao") ?: ""
            }
    }

    LaunchedEffect(bioSaved) {
        if (bioSaved) {
            Toast.makeText(context, "Bio atualizada!", Toast.LENGTH_SHORT).show()
            authViewModel.resetBioStatus()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isMyProfile) "Meu Perfil" else "Perfil de $nomeUsuario") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    tint = DonexBrown
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = nomeUsuario, style = MaterialTheme.typography.headlineSmall)
                Text(text = emailUsuario, style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(16.dp))

                if (isMyProfile) {
                    OutlinedTextField(
                        value = descricao,
                        onValueChange = { descricao = it },
                        label = { Text("Minha Bio") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                    Button(
                        onClick = { authViewModel.updateBio(descricao) },
                        modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = DonexBrown)
                    ) {
                        Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Salvar Bio")
                    }
                } else {
                    if (descricao.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = DonexBrown.copy(alpha = 0.05f))
                        ) {
                            Text(
                                text = descricao,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Divider()

                if (isMyProfile) {
                    Button(
                        onClick = {
                            authViewModel.logout()
                            onLogout()
                        },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("SAIR DA CONTA")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isMyProfile) "Minhas Postagens" else "Postagens de $nomeUsuario",
                        style = MaterialTheme.typography.titleMedium,
                        color = DonexBrown
                    )
                }
            }

            items(userPosts) { post ->
                PostCard(
                    post = post,
                    onChatClick = { },
                    onDeleteClick = { if (isMyProfile) mainViewModel.deletePost(post.id) },
                    onUserClick = { }
                )
            }
        }
    }
}