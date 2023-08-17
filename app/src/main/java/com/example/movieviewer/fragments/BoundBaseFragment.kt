package com.example.movieviewer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * @author Marko Nikolic on 17.8.23.
 */
abstract class BoundBaseFragment : Fragment() {

    protected abstract fun injectFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectFragment()
    }
}