package com.example.movieviewer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieviewer.repositories.LoginRepository
import javax.inject.Inject

/**
 * @author Marko Nikolic on 18.8.23.
 */
class ProfileViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {
    val successResult by lazy { MutableLiveData<Unit>() }

    private val _text = MutableLiveData<String>().apply {
        value = "This is profile Fragment" + loginRepository.userEmail
    }
    val text: LiveData<String> = _text

    fun logoutUser() {
        loginRepository.logout()
        successResult.postValue(Unit)
    }
}