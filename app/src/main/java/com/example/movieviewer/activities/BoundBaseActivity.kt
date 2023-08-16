package com.example.movieviewer.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.movieviewer.MovieViewerApplication

/**
 * @author Marko Nikolic on 16.8.23.
 */
abstract class BoundBaseActivity : AppCompatActivity() {

    /**
     * Called from the [.onCreate] method of the Activity that extends the BoundBaseActivity.
     */
    protected abstract fun injectActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            MovieViewerApplication[this].getAppComponent().inject(this)
        }
        injectActivity()
    }
}