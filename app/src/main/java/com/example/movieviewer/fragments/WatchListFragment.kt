package com.example.movieviewer.fragments

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.movieviewer.MovieViewerApplication
import com.example.movieviewer.activities.MovieDetailsActivity
import com.example.movieviewer.adapters.MovieCardAdapter
import com.example.movieviewer.adapters.WatchlistCardAdapter
import com.example.movieviewer.databinding.FragmentWatchlistBinding
import com.example.movieviewer.interfaces.ItemClickListener
import com.example.movieviewer.longToast
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

    val movieDetailsLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            getMovies()
        }
    }

    override fun injectFragment() {
        MovieViewerApplication[requireActivity()].getAppComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this,
                viewModelFactory)[WatchListViewModel::class.java.name, WatchListViewModel::class.java]
        binding.swipeRefreshLayout.setOnRefreshListener {
            getMovies()
        }
        initObservers()
        getMovies()
        return binding.root
    }

    private fun getMovies() {
        viewModel.getMovies()
    }

    private fun initObservers() {
        viewModel.apply {
            movieData.observe(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    binding.emptyView.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.emptyView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.recyclerView.adapter = WatchlistCardAdapter(it,
                            object : ItemClickListener {
                                override fun onItemClick(id: String) {
                                    movieDetailsLauncher.launch(
                                            MovieDetailsActivity.newInstance(requireActivity(), id))
                                }
                            })
                }
            }

            errorResult.observe(viewLifecycleOwner) {
                requireActivity().longToast(it)
            }

            isRefreshingResult.observe(viewLifecycleOwner) {
                binding.swipeRefreshLayout.isRefreshing = it
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}