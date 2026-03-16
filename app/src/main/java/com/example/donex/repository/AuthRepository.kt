package com.example.donex.repository

import com.example.donex.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun login(email: String, pass: String, onResult: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { onResult(it.isSuccessful) }
    }

    fun register(name: String, email: String, pass: String, onResult: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser?.uid ?: ""
                val user = User(uid = uid, name = name, email = email)
                db.collection("users").document(uid).set(user)
                    .addOnCompleteListener { onResult(it.isSuccessful) }
            } else {
                onResult(false)
            }
        }
    }

    fun updateBio(descricao: String, onResult: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        val data = mapOf("descricao" to descricao)
        db.collection("users").document(uid)
            .set(data, SetOptions.merge())
            .addOnCompleteListener { onResult(it.isSuccessful) }
    }

    fun logout() = auth.signOut()

    fun getCurrentUser() = auth.currentUser
}