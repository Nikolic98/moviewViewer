package com.example.movieviewer.modules

import com.example.movieviewer.dataSources.HomeDataSource
import com.example.movieviewer.dataSources.LoginDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Marko Nikolic on 24.8.23.
 */
@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun provideLoginDataSource() = LoginDataSource()

    @Provides
    @Singleton
    fun provideHomeDataSource() = HomeDataSource()
}