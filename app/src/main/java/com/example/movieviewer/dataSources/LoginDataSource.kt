package com.example.movieviewer.dataSources

import com.example.movieviewer.content.AppErrorObject
import com.example.movieviewer.models.LoggedInUser
import com.example.movieviewer.viewModels.results.ResultState
import com.example.movieviewer.viewModels.results.SuccessResultState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private var auth: FirebaseAuth = Firebase.auth

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

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUser(): LoggedInUser {
        val currentUser = auth.currentUser
        return LoggedInUser(
                currentUser!!.uid,
                currentUser.email!!)
    }
}