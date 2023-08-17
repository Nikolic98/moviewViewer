package com.example.movieviewer.modules

import androidx.lifecycle.ViewModel
import com.example.movieviewer.viewModels.CreateAccountViewModel
import com.example.movieviewer.viewModels.LoginViewModel
import com.example.movieviewer.viewModels.MainViewModel
import com.example.movieviewer.viewModels.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author Marko Nikolic on 16.8.23.
 */
@Module
abstract class ViewModelModules {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun provideMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun provideLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateAccountViewModel::class)
    abstract fun provideCreateAccountViewModel(viewModel: CreateAccountViewModel): ViewModel
}