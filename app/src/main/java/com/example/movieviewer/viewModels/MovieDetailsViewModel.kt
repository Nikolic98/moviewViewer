package com.example.movieviewer.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieviewer.models.Movie
import com.example.movieviewer.repositories.MovieRepository
import com.example.movieviewer.repositories.UserRepository
import com.example.movieviewer.viewModels.results.SuccessResultState
import kotlinx.coroutines.delay
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

    fun getMovie(movieId: String) {
        viewModelScope.launch {
            val result = movieRepository.getMovie(movieId)
            result.onSuccess {
                _movieData.postValue(it)
                delay(200)
                doesWatchlistContainMovie()
            }
            result.onFailure {
                errorResult.postValue(it.message)
            }
        }
    }

    fun addOrRemoveFromWatchlist(context: Context) {
        viewModelScope.launch {
            try {
                val result = userRepository.addOrRemoveFromWatchlist(context, movieData.value!!.id)
                if (result is SuccessResultState<*>) {
                    addToWatchListResult.postValue(result.result as String)
                    doesWatchlistContainMovie()
                }
            } catch (t: Throwable) {
                errorResult.postValue(t.localizedMessage)
            }
        }
    }

    private fun doesWatchlistContainMovie() {
        viewModelScope.launch {
            try {
                val result = userRepository.doesWatchlistContainMovie(movieData.value!!.id)
                if (result is SuccessResultState<*>) {
                    doesWatchlistContainMovieResult.postValue(result.result as Boolean)
                }
            } catch (t: Throwable) {
                errorResult.postValue(t.localizedMessage)
            }
        }
    }
}