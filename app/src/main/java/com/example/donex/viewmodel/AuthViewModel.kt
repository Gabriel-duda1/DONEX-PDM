package com.example.donex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donex.repository.AuthRepository
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _bioSaved = MutableStateFlow(false)
    val bioSaved = _bioSaved.asStateFlow()

    fun login(email: String, pass: String, onShowMain: () -> Unit) {
        _loading.value = true
        repository.login(email, pass) { success ->
            _loading.value = false
            if (success) onShowMain() else _error.value = "Falha no login. Verifique seus dados."
        }
    }

    fun register(name: String, email: String, pass: String, onShowMain: () -> Unit) {
        _loading.value = true
        repository.register(name, email, pass) { success ->
            if (success) {
                val user = repository.getCurrentUser()
                val profileUpdates = userProfileChangeRequest { displayName = name }
                user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                    _loading.value = false
                    onShowMain()
                }
            } else {
                _loading.value = false
                _error.value = "Erro ao cadastrar. Tente outro e-mail."
            }
        }
    }

    fun updateBio(descricao: String) {
        _bioSaved.value = false
        viewModelScope.launch {
            repository.updateBio(descricao) { success ->
                if (success) _bioSaved.value = true
            }
        }
    }

    fun resetBioStatus() { _bioSaved.value = false }

    fun logout() { repository.logout() }

    fun clearError() { _error.value = null }
}