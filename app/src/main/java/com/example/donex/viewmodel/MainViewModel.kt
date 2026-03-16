package com.example.donex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donex.model.Post
import com.example.donex.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val repository: PostRepository = PostRepository()) : ViewModel() {

    val posts: StateFlow<List<Post>> = repository.getPosts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isPosting = MutableStateFlow(false)
    val isPosting = _isPosting.asStateFlow()

    fun createPost(post: Post, imageUrl: String?, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isPosting.value = true
            try {
                val finalPost = if (!imageUrl.isNullOrBlank()) {
                    post.copy(imageUrl = imageUrl)
                } else {
                    post
                }

                repository.savePost(finalPost) { success ->
                    _isPosting.value = false
                    onComplete(success)
                }
            } catch (e: Exception) {
                _isPosting.value = false
                onComplete(false)
            }
        }
    }

    fun iniciarConversa(interessadoId: String, donoDoPostId: String, onChatReady: (String) -> Unit) {
        val ids = listOf(interessadoId, donoDoPostId).sorted()
        val chatId = "${ids[0]}_${ids[1]}"
        onChatReady(chatId)
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            repository.deletePost(postId) { }
        }
    }
}