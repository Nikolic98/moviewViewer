package com.example.movieviewer.modules

import com.example.movieviewer.MainActivity
import com.example.movieviewer.activities.BoundBaseActivity
import com.example.movieviewer.activities.MovieDetailsActivity
import com.example.movieviewer.activities.login.CreateAccountActivity
import com.example.movieviewer.activities.login.LoginActivity
import com.example.movieviewer.fragments.BoundBaseFragment
import com.example.movieviewer.fragments.HomeFragment
import com.example.movieviewer.fragments.ProfileFragment
import com.example.movieviewer.fragments.WatchListFragment
import dagger.Component
import javax.inject.Singleton

/**
 * @author Marko Nikolic on 16.8.23.
 */
@Singleton
@Component(
        modules = [
            DataSourceModule::class,
            RepositoryModule::class,
            ViewModelFactoryModule::class,
            ViewModelModules::class])
interface AppComponent {

    // Activities
    fun inject(boundBaseActivity: BoundBaseActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(createAccountActivity: CreateAccountActivity)
    fun inject(movieDetailsActivity: MovieDetailsActivity)

    // Fragments
    fun inject(boundBaseFragment: BoundBaseFragment)
    fun inject(homeFragment: HomeFragment)
    fun inject(watchListFragment: WatchListFragment)
    fun inject(profileFragment: ProfileFragment)
}