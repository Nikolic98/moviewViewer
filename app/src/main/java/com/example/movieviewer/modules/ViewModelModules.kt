package com.example.movieviewer.modules

import androidx.lifecycle.ViewModel
import com.example.movieviewer.viewModels.CreateAccountViewModel
import com.example.movieviewer.viewModels.HomeViewModel
import com.example.movieviewer.viewModels.LoginViewModel
import com.example.movieviewer.viewModels.MainViewModel
import com.example.movieviewer.viewModels.MovieDetailsViewModel
import com.example.movieviewer.viewModels.ProfileViewModel
import com.example.movieviewer.viewModels.SearchViewModel
import com.example.movieviewer.viewModels.ViewModelKey
import com.example.movieviewer.viewModels.WatchListViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun provideHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun provideSearchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WatchListViewModel::class)
    abstract fun provideWatchListViewModel(viewModel: WatchListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun provideProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    abstract fun provideMovieDetailsViewModel(viewModel: MovieDetailsViewModel): ViewModel
}