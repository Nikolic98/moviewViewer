package com.example.movieviewer.repositories

import com.example.movieviewer.dataSources.LoginDataSource
import com.example.movieviewer.models.LoggedInUser
import com.example.movieviewer.viewModels.results.ResultState
import com.example.movieviewer.viewModels.results.SuccessResultState

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun loginUser(username: String, password: String): ResultState {
        val result = dataSource.loginUser(username, password)

        if (result is SuccessResultState<*>) {
            setLoggedInUser(result.result as LoggedInUser)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}