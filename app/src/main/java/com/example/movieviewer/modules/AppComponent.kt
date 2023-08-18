package com.example.movieviewer.modules

import com.example.movieviewer.MainActivity
import com.example.movieviewer.activities.BoundBaseActivity
import com.example.movieviewer.activities.login.CreateAccountActivity
import com.example.movieviewer.activities.login.LoginActivity
import com.example.movieviewer.fragments.BoundBaseFragment
import com.example.movieviewer.fragments.HomeFragment
import com.example.movieviewer.fragments.ProfileFragment
import com.example.movieviewer.fragments.SearchFragment
import com.example.movieviewer.fragments.WatchListFragment
import dagger.Component
import javax.inject.Singleton

/**
 * @author Marko Nikolic on 16.8.23.
 */
@Singleton
@Component(modules = [ViewModelFactoryModule::class, ViewModelModules::class])
interface AppComponent {

    // Activities
    fun inject(boundBaseActivity: BoundBaseActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(createAccountActivity: CreateAccountActivity)

    // Fragments
    fun inject(boundBaseFragment: BoundBaseFragment)
    fun inject(homeFragment: HomeFragment)
    fun inject(searchFragment: SearchFragment)
    fun inject(watchListFragment: WatchListFragment)
    fun inject(profileFragment: ProfileFragment)
}