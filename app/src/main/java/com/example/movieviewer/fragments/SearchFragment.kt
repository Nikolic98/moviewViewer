package com.example.movieviewer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.movieviewer.MovieViewerApplication
import com.example.movieviewer.activities.MovieDetailsActivity
import com.example.movieviewer.adapters.ItemWithNameCardAdapter
import com.example.movieviewer.databinding.FragmentSearchBinding
import com.example.movieviewer.interfaces.ItemClickListener
import com.example.movieviewer.longToast
import com.example.movieviewer.viewModels.SearchViewModel
import com.example.movieviewer.viewModels.ViewModelFactory
import javax.inject.Inject

/**
 * @author Marko Nikolic on 18.8.23.
 */
class SearchFragment : BoundBaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchViewModel

    override fun injectFragment() {
        MovieViewerApplication[requireActivity()].getAppComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this,
                viewModelFactory)[SearchViewModel::class.java.name, SearchViewModel::class.java]

        binding.searchBar.hint = "Search"
        binding.searchView.hint = "Type a movie name"
        binding.searchView.setupWithSearchBar(binding.searchBar)

        binding.searchView.editText.addTextChangedListener {
            viewModel.searchMovie(it.toString().trim())
        }

        initObservers()
        return binding.root
    }

    private fun initObservers() {
        viewModel.apply {
            movieData.observe(viewLifecycleOwner) {
                binding.recyclerView.adapter = ItemWithNameCardAdapter(it,
                        object : ItemClickListener {
                            override fun onItemClick(id: String) {
                                startActivity(
                                        MovieDetailsActivity.newInstance(requireActivity(), id))
                            }
                        })
            }

            errorResult.observe(viewLifecycleOwner) {
                requireActivity().longToast(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}