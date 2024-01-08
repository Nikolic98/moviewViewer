package com.example.movieviewer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieviewer.models.Movie
import com.example.movieviewer.repositories.MovieRepository
import com.example.movieviewer.repositories.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Marko Nikolic on 18.8.23.
 */
class WatchListViewModel @Inject constructor(
        private val movieRepository: MovieRepository,
        private val userRepository: UserRepository
) : ViewModel() {

    private val _movieData = MutableLiveData<ArrayList<Movie>>()
    val movieData: LiveData<ArrayList<Movie>> = _movieData
    val errorResult by lazy { MutableLiveData<String>() }

    fun getMovies() {
        viewModelScope.launch {
            val movieIdsResult = userRepository.getIdsFromWatchList()
            movieIdsResult.onSuccess { movieIds ->
                val result = movieRepository.getMovies(movieIds)
                result.onSuccess {
                    _movieData.postValue(it)
                    delay(200)
                }
                result.onFailure {
                    errorResult.postValue(it.message)
                }
            }
            movieIdsResult.onFailure {
                errorResult.postValue(it.message)
            }
        }
    }
}