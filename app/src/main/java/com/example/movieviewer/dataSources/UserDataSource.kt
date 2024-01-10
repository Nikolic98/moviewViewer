package com.example.movieviewer.dataSources

import android.content.Context
import com.example.movieviewer.R
import com.example.movieviewer.content.AppErrorObject
import com.example.movieviewer.models.User
import com.example.movieviewer.viewModels.results.ResultState
import com.example.movieviewer.viewModels.results.SuccessResultState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList
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
        private const val USER_ID = "id"
        private const val WATCHLIST = "watchList"
    }

    suspend fun loginUser(username: String, password: String): Result<Unit> {
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.success(Unit))
                    } else {
                        continuation.resume(Result.failure(
                                AppErrorObject(task.exception?.message!!).joinForThrowable()))
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
                    it.documents.forEach { documentSnapshot ->
                        val user = documentSnapshot.toObject(User::class.java)
                        continuation.resume(Result.success(user!!))
                    }
                }
                .addOnFailureListener {
                    continuation.resume(Result.failure(it))
                }
        }
    }

    suspend fun addOrRemoveFromWatchlist(context: Context, id: String): ResultState {
        return suspendCoroutine { continuation ->
            val currentUser = auth.currentUser
            db.collection(USERS)
                .whereEqualTo(USER_ID, currentUser!!.uid)
                .get()
                .addOnSuccessListener {
                    it.documents[0].let { documentSnapshot ->
                        val user = documentSnapshot.toObject(User::class.java)
                        val addOrRemoveMessage: String
                        val addOrRemove = if (user?.watchList?.contains(id) == true) {
                            addOrRemoveMessage = context.getString(R.string.removed_from_watchlist)
                            FieldValue.arrayRemove(id)
                        } else {
                            addOrRemoveMessage = context.getString(R.string.added_to_watchlist)
                            FieldValue.arrayUnion(id)
                        }
                        db.collection(USERS)
                            .document(documentSnapshot.id)
                            .update(WATCHLIST, addOrRemove)
                            .addOnSuccessListener {
                                continuation.resume(SuccessResultState(addOrRemoveMessage))
                            }
                            .addOnFailureListener {
                                continuation.resumeWithException(
                                        AppErrorObject(it.message!!).joinForThrowable())
                            }
                    }
                }
                .addOnFailureListener {
                    continuation.resumeWithException(
                            AppErrorObject(it.message!!).joinForThrowable())
                }
        }
    }

    suspend fun doesWatchlistContainMovie(id: String): Result<Boolean> {
        return suspendCoroutine { continuation ->
            val currentUser = auth.currentUser
            db.collection(USERS)
                .whereEqualTo(USER_ID, currentUser!!.uid)
                .get()
                .addOnSuccessListener {
                    it.documents[0].let { documentSnapshot ->
                        val user = documentSnapshot.toObject(User::class.java)
                        val doesContain = user?.watchList?.contains(id) == true
                        continuation.resume(Result.success(doesContain))
                    }
                }
                .addOnFailureListener {
                    continuation.resume(
                            Result.failure(AppErrorObject(it.message!!).joinForThrowable()))
                }
        }
    }

    suspend fun getIdsFromWatchList(): Result<ArrayList<String>> {
        return suspendCoroutine { continuation ->
            val currentUser = auth.currentUser
            db.collection(USERS)
                .whereEqualTo(USER_ID, currentUser!!.uid)
                .get()
                .addOnSuccessListener {
                    it.documents.forEach { documentSnapshot ->
                        val user = documentSnapshot.toObject(User::class.java)
                        continuation.resume(
                                Result.success(user?.watchList) as Result<ArrayList<String>>)
                    }
                }
                .addOnFailureListener {
                    continuation.resume(
                            Result.failure(AppErrorObject(it.message!!).joinForThrowable()))
                }
        }
    }

    private fun saveUser(userId: String, name: String, username: String) {
        val user = User(userId, username, name, arrayListOf())
        db.collection(USERS).add(user)
    }

    suspend fun deleteUser(): Result<Unit> {
        return suspendCoroutine { continuation ->
            auth.currentUser?.let { firebaseUser ->
                db.collection(USERS)
                    .whereEqualTo(USER_ID, firebaseUser.uid)
                    .get()
                    .addOnSuccessListener { query ->
                        query.documents.forEach {
                            it.reference.delete()
                        }
                        firebaseUser.delete().addOnSuccessListener {
                            continuation.resume(Result.success(Unit))
                        }.addOnFailureListener {
                            continuation.resume(Result.failure(
                                    AppErrorObject(it.localizedMessage).joinForThrowable()))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(
                                Result.failure(
                                        AppErrorObject(it.localizedMessage).joinForThrowable()))
                    }
            }
        }
    }
}