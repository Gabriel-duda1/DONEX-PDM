package com.example.donex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donex.model.Chat
import com.example.donex.model.Message
import com.example.donex.repository.ChatRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository()

    val myChats: StateFlow<List<Chat>> = repository.getMyChats()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getMessages(chatId: String): StateFlow<List<Message>> {
        return repository.getMessages(chatId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    fun sendMessage(chatId: String, text: String) {
        if (text.isNotBlank()) {
            repository.sendMessage(chatId, text)
        }
    }

    fun startChatWithUser(otherId: String, otherName: String, onChatReady: (String) -> Unit) {
        repository.startOrGetChat(otherId, otherName, onChatReady)
    }
}