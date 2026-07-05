package com.viral.ai.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.viral.ai.model.User
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    val currentUser: FirebaseUser?
        get() = try { auth.currentUser } catch (e: Exception) { null }

    suspend fun signIn(email: String, password: String): AuthResult {
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signUp(email: String, password: String): AuthResult {
        return auth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun getUserProfile(): User? {
        val uid = currentUser?.uid ?: return null
        return try {
            db.collection("users").document(uid).get().await().toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveUserProfile(user: User) {
        val uid = currentUser?.uid ?: return
        try {
            db.collection("users").document(uid).set(user).await()
        } catch (e: Exception) {
            // Log or handle
        }
    }

    fun signOut() {
        try { auth.signOut() } catch (e: Exception) { }
    }
    
    suspend fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }
}
