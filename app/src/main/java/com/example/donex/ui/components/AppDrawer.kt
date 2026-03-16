package com.example.donex.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.donex.ui.theme.DonexBrown

@Composable
fun AppDrawer(
    onProfileClick: () -> Unit,
    onChatListClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "DONEX",
                style = MaterialTheme.typography.headlineMedium,
                color = DonexBrown
            )
            IconButton(onClick = onCloseClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Fechar Menu", tint = DonexBrown)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Divider()

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Meu Perfil") },
            selected = false,
            onClick = onProfileClick
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Chat, contentDescription = null) },
            label = { Text("Mensagens") },
            selected = false,
            onClick = onChatListClick
        )

        Spacer(modifier = Modifier.weight(1f))

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
            label = { Text("Sair do Aplicativo") },
            selected = false,
            onClick = onLogoutClick,
            colors = NavigationDrawerItemDefaults.colors(
                unselectedTextColor = Color.Red,
                unselectedIconColor = Color.Red
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}