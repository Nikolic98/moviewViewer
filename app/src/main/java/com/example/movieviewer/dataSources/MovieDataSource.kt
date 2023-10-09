package com.example.movieviewer.dataSources

import com.example.movieviewer.models.Movie
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @author Marko Nikolic on 9.10.23.
 */
class MovieDataSource {

    private val db = Firebase.firestore

    companion object {
        private const val MOVIES = "/movies"
    }

    suspend fun getMovie(movieId: String): Result<Movie> {
        return suspendCoroutine { continuation ->
            db.collection(MOVIES)
                .document(movieId)
                .get()
                .addOnSuccessListener {
                    it.toObject(Movie::class.java)?.let { movie ->
                        continuation.resume(Result.success(movie))
                    }
                }
                .addOnFailureListener {
                    continuation.resume(Result.failure(it))
                }
        }
    }
}