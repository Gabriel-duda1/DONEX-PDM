package com.example.donex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donex.model.Chat
import com.example.donex.model.Message
import com.example.donex.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository = ChatRepository()) : ViewModel() {

    val myChats: StateFlow<List<Chat>> = repository.getMyChats()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    fun loadMessages(chatId: String) {
        viewModelScope.launch {
            repository.getMessages(chatId).collect {
                _messages.value = it
            }
        }
    }

    fun sendMessage(chatId: String, text: String) {
        if (text.isNotBlank()) {
            viewModelScope.launch {
                repository.sendMessage(chatId, text)
            }
        }
    }

    fun startChatWithUser(otherId: String, otherName: String, onChatReady: (String) -> Unit) {
        viewModelScope.launch {
            repository.startOrGetChat(otherId, otherName) { chatId ->
                onChatReady(chatId)
            }
        }
    }
}