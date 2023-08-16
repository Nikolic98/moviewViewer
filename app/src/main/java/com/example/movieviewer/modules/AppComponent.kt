package com.example.movieviewer.modules

import com.example.movieviewer.MainActivity
import com.example.movieviewer.activities.BoundBaseActivity
import dagger.Component
import javax.inject.Singleton

/**
 * @author Marko Nikolic on 16.8.23.
 */
@Singleton
@Component(modules = [ViewModelFactoryModule::class, ViewModelModules::class])
interface AppComponent {

    fun inject(boundBaseActivity: BoundBaseActivity)
    fun inject(mainActivity: MainActivity)
}