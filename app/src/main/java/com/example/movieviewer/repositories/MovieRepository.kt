package com.example.movieviewer.repositories

import com.example.movieviewer.dataSources.MovieDataSource

/**
 * @author Marko Nikolic on 9.10.23.
 */
class MovieRepository(private val dataSource: MovieDataSource) {

    suspend fun getMovie(movieId: String) = dataSource.getMovie(movieId)

    suspend fun getMovies(movieIds: List<String>) = dataSource.getMovies(movieIds)

    suspend fun getAllMovies() = dataSource.getAllMovies()
}