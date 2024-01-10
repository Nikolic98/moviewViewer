package com.example.movieviewer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieviewer.models.Banner
import com.example.movieviewer.models.Genre
import com.example.movieviewer.models.ItemWithHeader
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

    private val _movieList = MutableLiveData<ArrayList<ItemWithHeader>>()
    val movieList: LiveData<ArrayList<ItemWithHeader>> = _movieList

    private val _banner = MutableLiveData<ArrayList<Banner>>()
    val banner: LiveData<ArrayList<Banner>> = _banner

    private val _searchMovieList = MutableLiveData<ArrayList<Movie>>()
    val searchMovieList: LiveData<ArrayList<Movie>> = _searchMovieList

    val errorResult by lazy { MutableLiveData<String>() }
    val isRefreshingResult by lazy { MutableLiveData<Boolean>() }

    private var genreListData = arrayListOf<Genre>()

    fun getData() {
        isRefreshingResult.value = true
        viewModelScope.launch {
            val genres = async { movieRepository.getMovieGenres() }
            val banner = async { homeRepository.getBanner() }
            val movies = async { movieRepository.getAllMovies() }
            genres.await().onSuccess {
                genreListData = it
            }.onFailure {
                isRefreshingResult.postValue(false)
                errorResult.postValue(it.localizedMessage)
            }
            banner.await().onSuccess {
                _banner.postValue(it)
            }.onFailure {
                isRefreshingResult.postValue(false)
                errorResult.postValue(it.localizedMessage)
            }
            movies.await().onSuccess { movieList ->
                isRefreshingResult.postValue(false)
                val movieWithHeader = arrayListOf<ItemWithHeader>()
                genreListData.forEach { genre ->
                    val filterMovies = movieList.filter { it.genreId == genre.id }
                    if (filterMovies.isEmpty()) return@forEach
                    movieWithHeader.add(ItemWithHeader(genre.name, filterMovies))
                }
                _movieList.postValue(movieWithHeader)
            }.onFailure {
                isRefreshingResult.postValue(false)
                errorResult.postValue(it.localizedMessage)
            }
        }
    }

    fun searchMovie(name: String) {
        viewModelScope.launch {
            movieRepository.getMoviesByName(name).onSuccess {
                _searchMovieList.postValue(it)
            }.onFailure {
                errorResult.postValue(it.localizedMessage)
            }
        }
    }
}