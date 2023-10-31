package com.example.movieviewer.repositories

import com.example.movieviewer.dataSources.UserDataSource
import com.example.movieviewer.models.LoggedInUser
import com.example.movieviewer.viewModels.results.ResultState
import com.example.movieviewer.viewModels.results.SuccessResultState

/**
 * @author Marko Nikolic on 24.8.23.
 */

class UserRepository(private val dataSource: UserDataSource) {

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