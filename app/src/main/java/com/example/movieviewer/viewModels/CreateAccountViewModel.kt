package com.example.movieviewer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieviewer.repositories.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Marko Nikolic on 17.8.23.
 */
class CreateAccountViewModel @Inject constructor(
        private val loginRepository: UserRepository) : ViewModel() {

    val successResult by lazy { MutableLiveData<Unit>() }
    val errorResult by lazy { MutableLiveData<String>() }

    fun createUser(name: String, username: String, password: String) {
        viewModelScope.launch {
            loginRepository.createNewUser(name, username, password)
                .onSuccess {
                    loginRepository.createNewUser(name, username, password)
                    successResult.postValue(Unit)
                }
                .onFailure { errorResult.postValue(it.localizedMessage) }
        }
    }
}