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

    // Transforma o Flow do Firebase em um StateFlow para a UI do Compose
    val posts: StateFlow<List<Post>> = repository.getPosts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isPosting = MutableStateFlow(false)
    val isPosting = _isPosting.asStateFlow()

    fun createPost(post: Post, onComplete: (Boolean) -> Unit) {
        _isPosting.value = true
        repository.savePost(post) { success ->
            _isPosting.value = false
            onComplete(success)
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            repository.deletePost(postId) { /* opcional: tratar erro */ }
        }
    }
}