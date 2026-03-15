package com.example.donex.repository

import com.example.donex.model.Chat
import com.example.donex.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val chats = snapshot.toObjects(Chat::class.java)
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
        val message = Message(senderId = currentUserId, text = text)

        val chatRef = db.collection("chats").document(chatId)

        db.runTransaction { transaction ->
            val messageRef = chatRef.collection("messages").document()
            transaction.set(messageRef, message)
            transaction.update(chatRef, "lastMessage", text)
            transaction.update(chatRef, "timestamp", System.currentTimeMillis())
        }
    }

    fun startOrGetChat(otherUserId: String, otherUserName: String, onComplete: (String) -> Unit) {
        val currentUserId = auth.currentUser?.uid ?: return
        val currentUserName = auth.currentUser?.displayName ?: "Utilizador"

        db.collection("chats")
            .whereArrayContains("userIds", currentUserId)
            .get()
            .addOnSuccessListener { snapshot ->
                val existingChat = snapshot.documents.find { doc ->
                    val ids = doc.get("userIds") as? List<*>
                    ids?.contains(otherUserId) == true
                }

                if (existingChat != null) {
                    onComplete(existingChat.id)
                } else {
                    val newChatRef = db.collection("chats").document()
                    val newChat = Chat(
                        id = newChatRef.id,
                        userIds = listOf(currentUserId, otherUserId),
                        userNames = mapOf(currentUserId to currentUserName, otherUserId to otherUserName)
                    )
                    newChatRef.set(newChat).addOnSuccessListener { onComplete(newChatRef.id) }
                }
            }
    }
}