package com.example.movieviewer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Marko Nikolic on 16.8.23.
 */
class MainViewModel @Inject constructor() : ViewModel() {

    fun testFun() {
        viewModelScope.launch {
        }
    }
}