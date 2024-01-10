package com.example.movieviewer.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieviewer.models.Movie
import com.example.movieviewer.repositories.MovieRepository
import com.example.movieviewer.repositories.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Marko Nikolic on 9.10.23.
 */
class MovieDetailsViewModel @Inject constructor(
        private val movieRepository: MovieRepository,
        private val userRepository: UserRepository) : ViewModel() {

    private val _movieData = MutableLiveData<Movie>()
    val movieData: LiveData<Movie> = _movieData

    val addToWatchListResult by lazy { MutableLiveData<String>() }
    val doesWatchlistContainMovieResult by lazy { MutableLiveData<Boolean>() }
    val errorResult by lazy { MutableLiveData<String>() }

    private lateinit var movieId: String

    fun getMovie(movieId: String) {
        viewModelScope.launch {
            val result = movieRepository.getMovie(movieId)
            result.onSuccess {
                _movieData.postValue(it)
                this@MovieDetailsViewModel.movieId = it.id
                doesWatchlistContainMovie()
            }
            result.onFailure {
                errorResult.postValue(it.localizedMessage)
            }
        }
    }

    fun addOrRemoveFromWatchlist(context: Context) {
        viewModelScope.launch {
            userRepository.addOrRemoveFromWatchlist(context, movieData.value!!.id).onSuccess {
                addToWatchListResult.postValue(it)
                doesWatchlistContainMovie()
            }.onFailure {
                errorResult.postValue(it.localizedMessage)
            }
        }
    }

    private fun doesWatchlistContainMovie() {
        viewModelScope.launch {
            if (!::movieId.isInitialized) {
                return@launch
            }
            val result = userRepository.doesWatchlistContainMovie(movieId)
            result.onSuccess {
                doesWatchlistContainMovieResult.postValue(it)
            }
            result.onFailure {
                errorResult.postValue(it.localizedMessage)
            }
        }
    }
}