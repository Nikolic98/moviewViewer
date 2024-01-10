package com.example.movieviewer.repositories

import android.content.Context
import com.example.movieviewer.dataSources.UserDataSource

/**
 * @author Marko Nikolic on 24.8.23.
 */

class UserRepository(private val dataSource: UserDataSource) {

    fun logout() {
        dataSource.logout()
    }

    suspend fun getCurrentUser() = dataSource.getCurrentUser()

    suspend fun createNewUser(name: String, username: String,
            password: String) = dataSource.createNewUser(name, username, password)

    suspend fun loginUser(username: String, password: String) = dataSource.loginUser(username,
            password)

    suspend fun addOrRemoveFromWatchlist(context: Context,
            id: String) = dataSource.addOrRemoveFromWatchlist(context, id)

    suspend fun doesWatchlistContainMovie(id: String) = dataSource.doesWatchlistContainMovie(id)

    suspend fun getIdsFromWatchList() = dataSource.getIdsFromWatchList()

    suspend fun deleteUser() = dataSource.deleteUser()
}