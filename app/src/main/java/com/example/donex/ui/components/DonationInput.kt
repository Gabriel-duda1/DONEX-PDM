package com.example.donex.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.donex.model.PostType
import com.example.donex.model.Post


@Composable
fun DonationInput(onPostClick: (String, PostType) -> Unit) {
    var text by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(PostType.OFFER) }

    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("O que você quer doar ou pedir?") }
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row {
                    RadioButton(selected = type == PostType.OFFER, onClick = { type = PostType.OFFER })
                    Text("Doar", modifier = Modifier.padding(top = 12.dp))
                    RadioButton(selected = type == PostType.REQUEST, onClick = { type = PostType.REQUEST })
                    Text("Pedir", modifier = Modifier.padding(top = 12.dp))
                }
                Button(onClick = { if(text.isNotBlank()) { onPostClick(text, type); text = "" } }) {
                    Text("Postar")
                }
            }
        }
    }
}