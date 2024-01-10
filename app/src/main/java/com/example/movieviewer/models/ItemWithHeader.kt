package com.example.movieviewer.models

/**
 * @author Marko Nikolic on 10.1.24.
 */
data class ItemWithHeader(
        val genreName: String = String(),
        val movies: List<Movie> = arrayListOf()
)