package com.example.movieviewer.dataSources

import com.example.movieviewer.models.Movie
import com.google.firebase.firestore.FieldPath
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

    suspend fun getMovies(movieIds: List<String>): Result<ArrayList<Movie>> {
        return suspendCoroutine { continuation ->
            val movies = arrayListOf<Movie>()
            if (movieIds.isEmpty()) {
                continuation.resume(Result.success(movies))
                return@suspendCoroutine
            }
            db.collection(MOVIES)
                .whereIn(FieldPath.documentId(), movieIds)
                .get()
                .addOnSuccessListener {
                    it.documents.forEach { documentSnapshot ->
                        documentSnapshot.toObject(Movie::class.java)?.let { movie ->
                            movies.add(movie)
                        }
                    }
                    continuation.resume(Result.success(movies))
                }
                .addOnFailureListener {
                    continuation.resume(Result.failure(it))
                }
        }
    }

    suspend fun getAllMovies(): Result<ArrayList<Movie>> {
        return suspendCoroutine { continuation ->
            db.collection(MOVIES)
                .get()
                .addOnSuccessListener {
                    val movies = arrayListOf<Movie>()
                    it.documents.forEach { documentSnapshot ->
                        documentSnapshot.toObject(Movie::class.java)?.let { movie ->
                            movies.add(movie)
                        }
                    }
                    continuation.resume(Result.success(movies))
                }
                .addOnFailureListener {
                    continuation.resume(Result.failure(it))
                }
        }
    }
}