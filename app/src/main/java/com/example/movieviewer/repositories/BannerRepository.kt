package com.example.movieviewer.repositories

import com.example.movieviewer.dataSources.BannerDataSource

/**
 * @author Marko Nikolic on 30.8.23.
 */
class BannerRepository(private val dataSource: BannerDataSource) {

    suspend fun getBanner() = dataSource.getBanner()
}