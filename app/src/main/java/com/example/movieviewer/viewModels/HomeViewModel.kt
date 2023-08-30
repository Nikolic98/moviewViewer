package com.example.movieviewer.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.movieviewer.models.Banner
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

    private val _banner = MutableLiveData<ArrayList<Banner>>()
    val banner: LiveData<ArrayList<Banner>> = _banner

    val successResult by lazy { MutableLiveData<Unit>() }
    val errorResult by lazy { MutableLiveData<String>() }

    fun getBanner() {
        viewModelScope.launch {
            val result = homeRepository.getBanner()
            result.onSuccess {
                _banner.postValue(it)
            }
            result.onFailure {
                errorResult.postValue(it.message)
            }
        }
    }

    fun getMovies() {
        viewModelScope.launch {
            val result = homeRepository.getMovies()
            result.onSuccess {
                _movieList.postValue(it)
            }
            result.onFailure {
                errorResult.postValue(it.message)
            }
        }
    }
}