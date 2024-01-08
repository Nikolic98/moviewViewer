package com.example.movieviewer.models

import kotlin.collections.ArrayList

/**
 * @author Marko Nikolic on 25.8.23.
 */
data class User(
        val userId: String = String(),
        val email: String = String(),
        val displayName: String = String(),
        val watchList: ArrayList<String> = arrayListOf())