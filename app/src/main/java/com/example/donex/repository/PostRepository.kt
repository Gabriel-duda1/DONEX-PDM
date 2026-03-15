package com.example.donex.repository

import com.example.donex.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PostRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun getPosts(): Flow<List<Post>> = callbackFlow {
        val listener = db.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val posts = snapshot.toObjects(Post::class.java)
                    trySend(posts)
                }
            }
        awaitClose { listener.remove() }
    }

    fun savePost(post: Post, onComplete: (Boolean) -> Unit) {
        val currentUser = auth.currentUser ?: return
        val ref = db.collection("posts").document()
        val finalPost = post.copy(
            id = ref.id,
            userId = currentUser.uid,
            userName = currentUser.displayName ?: "Usuário",
            userEmail = currentUser.email ?: ""
        )
        ref.set(finalPost).addOnCompleteListener { onComplete(it.isSuccessful) }
    }

    fun deletePost(postId: String, onComplete: (Boolean) -> Unit) {
        db.collection("posts").document(postId).delete()
            .addOnCompleteListener { onComplete(it.isSuccessful) }
    }
}