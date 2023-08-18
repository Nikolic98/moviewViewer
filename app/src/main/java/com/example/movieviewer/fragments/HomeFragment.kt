package com.example.movieviewer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.movieviewer.MovieViewerApplication
import com.example.movieviewer.databinding.FragmentHomeBinding
import com.example.movieviewer.viewModels.HomeViewModel
import com.example.movieviewer.viewModels.ViewModelFactory
import javax.inject.Inject

/**
 * @author Marko Nikolic on 18.8.23.
 */
class HomeFragment : BoundBaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel

    override fun injectFragment() {
        MovieViewerApplication[requireActivity()].getAppComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this,
                viewModelFactory)[HomeViewModel::class.java.name, HomeViewModel::class.java]
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