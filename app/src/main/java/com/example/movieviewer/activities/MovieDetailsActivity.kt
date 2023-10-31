package com.example.movieviewer.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.movieviewer.MovieViewerApplication
import com.example.movieviewer.R
import com.example.movieviewer.databinding.ActivityMovieDetailsBinding
import com.example.movieviewer.longToast
import com.example.movieviewer.viewModels.MovieDetailsViewModel
import com.example.movieviewer.viewModels.ViewModelFactory
import javax.inject.Inject

class MovieDetailsActivity : BoundBaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MovieDetailsViewModel

    private lateinit var binding: ActivityMovieDetailsBinding

    companion object {
        private const val KEY_MOVIE_ID = "key:movie_id"

        fun newInstance(context: Context, movieId: String): Intent {
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra(KEY_MOVIE_ID, movieId)
            return intent
        }
    }

    override fun injectActivity() {
        MovieViewerApplication[this].getAppComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this,
                viewModelFactory)[MovieDetailsActivity::class.java.name, MovieDetailsViewModel::class.java]
        setContentView(binding.root)

        binding.toolbar.apply {
            title = getString(R.string.movie_details)
            setNavigationOnClickListener {
                finish()
            }
        }

        binding.watchlistBtn.setOnClickListener {
            viewModel.addOrRemoveFromWatchlist(this).toString()
        }

        initObservers()

        intent.getStringExtra(KEY_MOVIE_ID)?.let {
            viewModel.getMovie(it)
        }
    }

    private fun initObservers() {
        viewModel.apply {
            movieData.observe(this@MovieDetailsActivity) {
                Glide.with(binding.root)
                    .load(it.imageUrl)
                    .into(binding.image)
                binding.name.text = it.name
                binding.description.text = it.description
            }
            errorResult.observe(this@MovieDetailsActivity) {
                longToast(it)
            }
            addToWatchListResult.observe(this@MovieDetailsActivity) {
                longToast(it)
            }
            doesWatchlistContainMovieResult.observe(this@MovieDetailsActivity) { doesContain ->
                val drawable = if (doesContain) getDrawable(R.drawable.ic_added) else getDrawable(R.drawable.ic_add)
                binding.watchlistBtn.background = drawable
            }
        }
    }
}