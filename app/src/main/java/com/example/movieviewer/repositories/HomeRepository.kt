package com.example.movieviewer.repositories

import com.example.movieviewer.dataSources.HomeDataSource

/**
 * @author Marko Nikolic on 30.8.23.
 */
class HomeRepository(private val dataSource: HomeDataSource) {

    suspend fun getBanner() = dataSource.getBanner()

    suspend fun getMovies() = dataSource.getMovies()
}