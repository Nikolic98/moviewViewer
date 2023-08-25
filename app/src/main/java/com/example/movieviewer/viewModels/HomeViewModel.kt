package com.example.movieviewer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.UUID
import javax.inject.Inject

/**
 * @author Marko Nikolic on 18.8.23.
 */
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment + ${UUID.randomUUID()}"
    }
    val text: LiveData<String> = _text
}