package com.example.movieviewer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieviewer.models.Movie
import com.example.movieviewer.repositories.HomeRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Marko Nikolic on 18.8.23.
 */
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {

    private val _movieList = MutableLiveData<ArrayList<Movie>>()
    val movieList: LiveData<ArrayList<Movie>> = _movieList

    val successResult by lazy { MutableLiveData<Unit>() }
    val errorResult by lazy { MutableLiveData<String>() }

    fun getMovies() {
        viewModelScope.launch {
            val result = homeRepository.getMovies()
            result.onSuccess {
                _movieList.postValue(it)
                println("Broj filmova: ${it.size}")
            }
            result.onFailure {
                errorResult.postValue(it.message)
            }
        }
    }
}