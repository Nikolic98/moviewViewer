package com.example.movieviewer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieviewer.repositories.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Marko Nikolic on 18.8.23.
 */
class ProfileViewModel @Inject constructor(
        private val loginRepository: UserRepository) : ViewModel() {
    val successResult by lazy { MutableLiveData<Unit>() }

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    fun getCurrentUser() {
        viewModelScope.launch {
            val result = loginRepository.getCurrentUser()
            result.onSuccess {
                _text.postValue("User email: ${it.email}")
            }
        }
    }

    fun logoutUser() {
        loginRepository.logout()
        successResult.postValue(Unit)
    }
}