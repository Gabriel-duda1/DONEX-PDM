package com.example.donex.ui.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(onProfileClick: () -> Unit, onLogoutClick: () -> Unit) {
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(16.dp))
        Text("DONEX Menu", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
        Divider()
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Meu Perfil") },
            selected = false,
            onClick = onProfileClick
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
            label = {Text("Sair") },
            selected = false,
            onClick = onLogoutClick
        )
    }
}