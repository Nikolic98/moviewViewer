package com.example.movieviewer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieviewer.models.Movie
import com.example.movieviewer.repositories.MovieRepository
import com.example.movieviewer.repositories.UserRepository
import kotlinx.coroutines.async
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
    val isRefreshingResult by lazy { MutableLiveData<Boolean>() }

    fun getMovies() {
        isRefreshingResult.value = true
        viewModelScope.launch {
            val moviesIds = async { userRepository.getIdsFromWatchList() }.await()
            moviesIds.onSuccess { ids ->
                movieRepository.getMovies(ids).onSuccess {
                    _movieData.postValue(it)
                    isRefreshingResult.postValue(false)
                }.onFailure {
                    errorResult.postValue(it.message)
                    isRefreshingResult.postValue(false)
                }
            }
            moviesIds.onFailure {
                errorResult.postValue(it.message)
                isRefreshingResult.postValue(false)
            }
        }
    }
}