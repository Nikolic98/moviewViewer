package com.example.movieviewer.modules

import com.example.movieviewer.dataSources.LoginDataSource
import com.example.movieviewer.repositories.LoginRepository
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
    fun provideLoginRepository(loginDataSource: LoginDataSource) = LoginRepository(loginDataSource)
}