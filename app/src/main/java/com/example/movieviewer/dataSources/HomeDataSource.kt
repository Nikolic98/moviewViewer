package com.example.movieviewer.dataSources

import com.example.movieviewer.models.Movie
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @author Marko Nikolic on 30.8.23.
 */
class HomeDataSource {

    private val db = Firebase.firestore

    companion object {
        private const val MOVIES = "/movies"
    }

    suspend fun getMovies(): Result<ArrayList<Movie>> {
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