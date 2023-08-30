package com.example.movieviewer.repositories

import com.example.movieviewer.dataSources.LoginDataSource
import com.example.movieviewer.models.LoggedInUser
import com.example.movieviewer.viewModels.results.ResultState
import com.example.movieviewer.viewModels.results.SuccessResultState

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(private val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    private var currentUser: LoggedInUser? = null
        private set

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        currentUser = null
    }

    fun logout() {
        currentUser = null
        dataSource.logout()
    }

    suspend fun getCurrentUser() = dataSource.getCurrentUser()

    suspend fun createNewUser(name: String, username: String, password: String): ResultState {
        return dataSource.createNewUser(name, username, password)
    }

    suspend fun loginUser(username: String, password: String): ResultState {
        val result = dataSource.loginUser(username, password)

        if (result is SuccessResultState<*>) {
            setLoggedInUser(result.result as LoggedInUser)
        }
        return result
    }

    // todo maybe remove this ?
    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.currentUser = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}