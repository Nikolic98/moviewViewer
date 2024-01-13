package com.example.movieviewer.utils

import android.util.Patterns

/**
 * @author Marko Nikolic on 13.1.24.
 */
object TextValidationHelper {

    fun isUsernameValid(username: String): Boolean {
        return if (username.isNotBlank()) {
            if (username.contains('@')) {
                Patterns.EMAIL_ADDRESS.matcher(username).matches()
            } else {
                false
            }
        } else {
            false
        }
    }

    fun isPasswordValid(password: String) = password.length > 5
}