package com.example.donex.repository

import com.example.donex.model.Chat
import com.example.donex.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ChatRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun getMyChats(): Flow<List<Chat>> = callbackFlow {
        val currentUserId = auth.currentUser?.uid ?: ""
        val listener = db.collection("chats")
            .whereArrayContains("userIds", currentUserId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val chats = snapshot.documents.mapNotNull { it.toObject(Chat::class.java)?.copy(id = it.id) }
                    trySend(chats)
                }
            }
        awaitClose { listener.remove() }
    }

    fun getMessages(chatId: String): Flow<List<Message>> = callbackFlow {
        val listener = db.collection("chats").document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val messages = snapshot.toObjects(Message::class.java)
                    trySend(messages)
                }
            }
        awaitClose { listener.remove() }
    }

    fun sendMessage(chatId: String, text: String) {
        val currentUserId = auth.currentUser?.uid ?: return
        val agora = System.currentTimeMillis()

        val message = Message(
            senderId = currentUserId,
            text = text,
            timestamp = agora
        )

        val chatRef = db.collection("chats").document(chatId)

        val chatUpdate = mapOf(
            "lastMessage" to text,
            "timestamp" to agora,
            "userIds" to chatId.split("_")
        )

        chatRef.set(chatUpdate, SetOptions.merge()).addOnSuccessListener {
            chatRef.collection("messages").add(message)
        }
    }

    fun startOrGetChat(otherUserId: String, otherUserName: String, onComplete: (String) -> Unit) {
        val currentUserId = auth.currentUser?.uid ?: return
        val chatId = if (currentUserId < otherUserId) "${currentUserId}_${otherUserId}" else "${otherUserId}_${currentUserId}"
        val chatRef = db.collection("chats").document(chatId)

        chatRef.get().addOnSuccessListener { doc ->
            if (doc.exists()) {
                onComplete(chatId)
            } else {
                db.collection("users").document(currentUserId).get().addOnSuccessListener { userDoc ->
                    val myRealName = userDoc.getString("name") ?: "Usuário"

                    val newChat = Chat(
                        id = chatId,
                        userIds = listOf(currentUserId, otherUserId),
                        userNames = mapOf(
                            currentUserId to myRealName,
                            otherUserId to otherUserName
                        ),
                        timestamp = System.currentTimeMillis(),
                        lastMessage = "Conversa iniciada"
                    )
                    chatRef.set(newChat).addOnSuccessListener { onComplete(chatId) }
                }
            }
        }
    }
}