package com.example.movieviewer.modules

import androidx.lifecycle.ViewModelProvider
import com.example.movieviewer.viewModels.ViewModelFactory
import dagger.Binds
import dagger.Module

/**
 * @author Marko Nikolic on 16.8.23.
 */
@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}