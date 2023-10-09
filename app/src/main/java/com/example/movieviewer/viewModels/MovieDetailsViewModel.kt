package com.example.movieviewer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieviewer.models.Movie
import com.example.movieviewer.repositories.MovieRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Marko Nikolic on 9.10.23.
 */
class MovieDetailsViewModel @Inject constructor(
        private val movieRepository: MovieRepository) : ViewModel() {

    private val _movieData = MutableLiveData<Movie>()
    val movieData: LiveData<Movie> = _movieData

    val successResult by lazy { MutableLiveData<Unit>() }
    val errorResult by lazy { MutableLiveData<String>() }

    fun getMovie(movieId: String) {
        viewModelScope.launch {
            val result = movieRepository.getMovie(movieId)
            result.onSuccess {
                _movieData.postValue(it)
            }
            result.onFailure {
                errorResult.postValue(it.message)
            }
        }
    }

    fun addToWatchList(): String? {
        return movieData.value?.id
    }
}