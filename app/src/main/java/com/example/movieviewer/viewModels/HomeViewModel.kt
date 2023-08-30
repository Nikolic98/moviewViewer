package com.example.movieviewer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieviewer.repositories.HomeRepository
import java.util.UUID
import javax.inject.Inject

/**
 * @author Marko Nikolic on 18.8.23.
 */
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment + ${UUID.randomUUID()}"
    }
    val text: LiveData<String> = _text

    private fun getMovies() {

    }
}