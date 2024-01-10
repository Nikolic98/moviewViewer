package com.example.movieviewer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.movieviewer.MovieViewerApplication
import com.example.movieviewer.activities.MovieDetailsActivity
import com.example.movieviewer.adapters.BannerAdapter
import com.example.movieviewer.adapters.ItemWithNameCardAdapter
import com.example.movieviewer.adapters.MovieCardAdapter
import com.example.movieviewer.databinding.FragmentHomeBinding
import com.example.movieviewer.interfaces.ItemClickListener
import com.example.movieviewer.longToast
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
            savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this,
                viewModelFactory)[HomeViewModel::class.java.name, HomeViewModel::class.java]

        binding.swipeRefreshLayout.setOnRefreshListener {
            initialRequest()
        }

        binding.searchView.apply {
            setupWithSearchBar(binding.searchBar)
            editText.addTextChangedListener {
                viewModel.searchMovie(it.toString().trim())
            }
        }

        initObservers()
        initialRequest()
        return binding.root
    }

    private fun initialRequest() {
        viewModel.getData()
    }

    private fun initObservers() {
        viewModel.apply {
            // refactor from recyclerView to ViewPager
            banner.observe(viewLifecycleOwner) {
                binding.recyclerViewBanner.adapter = BannerAdapter(it,
                        object : ItemClickListener {
                            override fun onItemClick(id: String) {
                                startDetails(id)
                            }
                        })
            }
            movieList.observe(viewLifecycleOwner) {
                binding.recyclerView.adapter = MovieCardAdapter(it,
                        object : ItemClickListener {
                            override fun onItemClick(id: String) {
                                startDetails(id)
                            }
                        })
            }
            searchMovieList.observe(viewLifecycleOwner) {
                binding.recyclerViewSearch.adapter = ItemWithNameCardAdapter(it,
                        object : ItemClickListener {
                            override fun onItemClick(id: String) {
                                startDetails(id)
                            }
                        })
            }
            errorResult.observe(viewLifecycleOwner) {
                activity?.longToast(it)
            }
            isRefreshingResult.observe(viewLifecycleOwner) {
                binding.swipeRefreshLayout.isRefreshing = it
            }
        }
    }

    private fun startDetails(movieId: String) {
        startActivity(MovieDetailsActivity.newInstance(requireActivity(), movieId))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}