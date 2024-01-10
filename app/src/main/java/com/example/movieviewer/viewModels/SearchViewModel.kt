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
 * @author Marko Nikolic on 18.8.23.
 */
class SearchViewModel @Inject constructor(
        private val movieRepository: MovieRepository) : ViewModel() {

    private val _movieData = MutableLiveData<ArrayList<Movie>>()
    val movieData: LiveData<ArrayList<Movie>> = _movieData
    val errorResult by lazy { MutableLiveData<String>() }

    fun searchMovie(name: String) {
        viewModelScope.launch {
            movieRepository.getMoviesByName(name).onSuccess {
                _movieData.postValue(it)
            }.onFailure {
                errorResult.postValue(it.localizedMessage)
            }
        }
    }

}