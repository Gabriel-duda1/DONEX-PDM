package com.example.donex.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.donex.model.PostType

@Composable
fun DonationInput(onPostClick: (String, PostType, String?) -> Unit) {
    var text by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(PostType.OFFER) }
    var showUrlField by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("O que você quer doar ou pedir?") }
            )

            if (showUrlField) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Link da imagem (URL)") },
                    placeholder = { Text("https://exemplo.com/foto.jpg") },
                    leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { showUrlField = !showUrlField }) {
                        Icon(
                            Icons.Default.Link,
                            contentDescription = "Adicionar Link",
                            tint = if (showUrlField) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    RadioButton(selected = type == PostType.OFFER, onClick = { type = PostType.OFFER })
                    Text("Doar")
                    RadioButton(selected = type == PostType.REQUEST, onClick = { type = PostType.REQUEST })
                    Text("Pedir")
                }
                Button(onClick = {
                    if (text.isNotBlank()) {
                        onPostClick(text, type, imageUrl.ifBlank { null })
                        text = ""
                        imageUrl = ""
                        showUrlField = false
                    }
                }) {
                    Text("Postar")
                }
            }
        }
    }
}