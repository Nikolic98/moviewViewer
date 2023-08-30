package com.example.movieviewer.models

/**
 * @author Marko Nikolic on 25.8.23.
 */
data class Movie(
        val id: String = String(),
        val name: String = String(),
        val description: String = String(),
        val imageUrl: String = String()
)