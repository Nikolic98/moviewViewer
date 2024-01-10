package com.example.movieviewer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private lateinit var bannerAdapter: BannerAdapter
    private var currentBannerItem = 0
    private var autoScrollJob: Job? = null

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
                bannerAdapter = BannerAdapter(it,
                        object : ItemClickListener {
                            override fun onItemClick(id: String) {
                                startDetails(id)
                            }
                        })
                binding.recyclerViewBanner.adapter = bannerAdapter
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

    private fun startAutoScroll() {
        autoScrollJob = lifecycleScope.launch {
            while (isActive) {
                delay(3000)

                withContext(Dispatchers.Main) {
                    if (bannerAdapter.itemCount > 0) {
                        if (currentBannerItem == bannerAdapter.itemCount - 1) {
                            currentBannerItem = 0
                        } else {
                            currentBannerItem++
                        }
                        binding.recyclerViewBanner.smoothScrollToPosition(currentBannerItem)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        autoScrollJob?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}