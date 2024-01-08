package com.example.movieviewer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieviewer.models.Banner
import com.example.movieviewer.models.Movie
import com.example.movieviewer.repositories.HomeRepository
import com.example.movieviewer.repositories.MovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Marko Nikolic on 18.8.23.
 */
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository,
        private val movieRepository: MovieRepository) : ViewModel() {

    private val _movieList = MutableLiveData<ArrayList<Movie>>()
    val movieList: LiveData<ArrayList<Movie>> = _movieList

    private val _banner = MutableLiveData<ArrayList<Banner>>()
    val banner: LiveData<ArrayList<Banner>> = _banner

    val errorResult by lazy { MutableLiveData<String>() }
    val isRefreshingResult by lazy { MutableLiveData<Boolean>() }

    fun getData() {
        isRefreshingResult.value = true
        viewModelScope.launch {
            val banner = async { homeRepository.getBanner() }
            val movies = async { movieRepository.getAllMovies() }
            banner.await().onSuccess {
                _banner.postValue(it)
            }.onFailure {
                isRefreshingResult.postValue(false)
                errorResult.postValue(it.message)
            }
            movies.await().onSuccess {
                isRefreshingResult.postValue(false)
                _movieList.postValue(it)
            }.onFailure {
                isRefreshingResult.postValue(false)
                errorResult.postValue(it.message)
            }
        }
    }
}