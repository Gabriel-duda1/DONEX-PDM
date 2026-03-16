package com.example.donex.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.donex.model.Post
import com.example.donex.model.PostType
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.donex.ui.theme.DonexBrown

@Composable
fun PostCard(
    post: Post,
    onChatClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onUserClick: () -> Unit
) {
    val azulBebe = Color(0xFFB3E5FC)
    val marromEtiqueta = Color(0xFFD7CCC8)

    val currentUserId = Firebase.auth.currentUser?.uid
    val isOwner = post.userId == currentUserId

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.clickable { onUserClick() }) {
                    Text(
                        text = post.userName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = DonexBrown
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = if (post.tipo == PostType.OFFER) azulBebe else marromEtiqueta,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = if (post.tipo == PostType.OFFER) "DOAÇÃO" else "PEDIDO",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    if (isOwner) {
                        IconButton(onClick = onDeleteClick) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Deletar Post",
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (!post.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = "Imagem da doação",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(bottom = 8.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Text(text = post.conteudo, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))

            if (!isOwner) {
                Button(
                    onClick = onChatClick,
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(containerColor = DonexBrown)
                ) {
                    Text("TENHO INTERESSE", color = Color.White)
                }
            }
        }
    }
}