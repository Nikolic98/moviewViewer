package com.example.movieviewer.dataSources

import com.example.movieviewer.content.AppErrorObject
import com.example.movieviewer.models.LoggedInUser
import com.example.movieviewer.models.User
import com.example.movieviewer.viewModels.results.ResultState
import com.example.movieviewer.viewModels.results.SuccessResultState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author Marko Nikolic on 24.8.23.
 */
class UserDataSource {

    private var auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore

    companion object {
        private const val USERS = "/users"
        private const val USER_ID = "userId"
    }

    suspend fun loginUser(username: String, password: String): ResultState {
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentUser = auth.currentUser
                        val user = LoggedInUser(
                                currentUser!!.uid,
                                currentUser.email!!)
                        continuation.resume(SuccessResultState(user))
                    } else {
                        continuation.resumeWithException(
                                AppErrorObject(task.exception?.message!!).joinForThrowable())
                    }
                }
        }
    }

    suspend fun createNewUser(name: String, username: String, password: String): ResultState {
        return suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentUser = auth.currentUser
                        saveUser(currentUser!!.uid, name, username)
                        continuation.resume(SuccessResultState(Unit))
                    } else {
                        val input = task.exception?.message!!
                        val startIndex = input.indexOf('[')
                        val endIndex = input.indexOf(']')
                        val errorMessage = if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                            input.substring(startIndex + 1, endIndex)
                        } else {
                            input
                        }
                        continuation.resumeWithException(
                                AppErrorObject(errorMessage).joinForThrowable())
                    }
                }
        }
    }

    fun logout() {
        auth.signOut()
    }

    suspend fun getCurrentUser(): Result<User> {
        return suspendCoroutine { continuation ->
            val currentUser = auth.currentUser
            db.collection(USERS)
                .whereEqualTo(USER_ID, currentUser!!.uid)
                .get()
                .addOnSuccessListener {
                    val documentSnapshot = it.documents[0]
                    val user = documentSnapshot.toObject(User::class.java)
                    continuation.resume(Result.success(user!!))
                }
                .addOnFailureListener {
                    continuation.resume(Result.failure(it))
                }
        }
    }

    private fun saveUser(userId: String, name: String, username: String) {
        val user = User(userId, username, name, arrayListOf())
        db.collection(USERS).add(user)
    }
}