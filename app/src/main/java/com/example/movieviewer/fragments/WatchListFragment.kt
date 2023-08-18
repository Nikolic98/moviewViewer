package com.example.movieviewer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.movieviewer.MovieViewerApplication
import com.example.movieviewer.databinding.FragmentWatchlistBinding
import com.example.movieviewer.viewModels.ViewModelFactory
import com.example.movieviewer.viewModels.WatchListViewModel
import javax.inject.Inject

/**
 * @author Marko Nikolic on 18.8.23.
 */
class WatchListFragment : BoundBaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: WatchListViewModel

    override fun injectFragment() {
        MovieViewerApplication[requireActivity()].getAppComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this,
                viewModelFactory)[WatchListViewModel::class.java.name, WatchListViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}