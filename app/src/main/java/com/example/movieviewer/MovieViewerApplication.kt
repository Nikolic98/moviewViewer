package com.example.movieviewer

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LifecycleObserver
import com.example.movieviewer.modules.AppComponent
import com.example.movieviewer.modules.DaggerAppComponent
import com.example.movieviewer.modules.DataSourceModule
import com.example.movieviewer.modules.RepositoryModule
import com.example.movieviewer.utils.PrefHelper

/**
 * @author Marko Nikolic on 16.8.23.
 */

class MovieViewerApplication : Application(), LifecycleObserver {
    companion object {
        @JvmStatic
        operator fun get(context: Context): MovieViewerApplication {
            return context.applicationContext as MovieViewerApplication
        }

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    private lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        initAppComponent()
        PrefHelper.initPrefs(applicationContext, getString(R.string.shared_prefs), MODE_PRIVATE)
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
            .dataSourceModule(DataSourceModule())
            .repositoryModule(RepositoryModule())
            .build()
    }

    fun getAppComponent(): AppComponent {
        if (!::appComponent.isInitialized) {
            initAppComponent()
        }
        return appComponent
    }
}