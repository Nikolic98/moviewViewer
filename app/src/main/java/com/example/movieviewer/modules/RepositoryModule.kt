package com.example.movieviewer.modules

import com.example.movieviewer.dataSources.HomeDataSource
import com.example.movieviewer.dataSources.UserDataSource
import com.example.movieviewer.dataSources.MovieDataSource
import com.example.movieviewer.repositories.HomeRepository
import com.example.movieviewer.repositories.UserRepository
import com.example.movieviewer.repositories.MovieRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Marko Nikolic on 24.8.23.
 */

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepository(loginDataSource: UserDataSource) = UserRepository(loginDataSource)

    @Provides
    @Singleton
    fun provideHomeRepository(homeDataSource: HomeDataSource) = HomeRepository(homeDataSource)

    @Provides
    @Singleton
    fun provideMovieRepository(movieDataSource: MovieDataSource) = MovieRepository(movieDataSource)
}