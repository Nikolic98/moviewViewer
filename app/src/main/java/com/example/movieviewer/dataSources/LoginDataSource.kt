package com.example.movieviewer.dataSources

import com.example.movieviewer.content.AppErrorObject
import com.example.movieviewer.models.LoggedInUser
import com.example.movieviewer.viewModels.results.ResultState
import com.example.movieviewer.viewModels.results.SuccessResultState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private lateinit var auth: FirebaseAuth

    suspend fun loginUser(username: String, password: String): ResultState {
        return suspendCoroutine { continuation ->
            auth = Firebase.auth
            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        //                    val user = auth.currentUser
                        //                    updateUI(user)
                        // todo still in test
                        val fakeUser = LoggedInUser(UUID.randomUUID().toString(), "Jane Doe")
                        continuation.resume(SuccessResultState(fakeUser))
                    } else {
                        continuation.resumeWithException(
                                AppErrorObject(task.exception?.message!!).joinForThrowable())
                    }
                }
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}