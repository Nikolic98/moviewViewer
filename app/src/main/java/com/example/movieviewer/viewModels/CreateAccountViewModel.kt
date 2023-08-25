package com.example.movieviewer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieviewer.repositories.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Marko Nikolic on 17.8.23.
 */
class CreateAccountViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {

    val successResult by lazy { MutableLiveData<Unit>() }
    val errorResult by lazy { MutableLiveData<String>() }

    fun createUser(name: String, username: String, password: String) {
        viewModelScope.launch {
            try {
                loginRepository.createNewUser(name, username, password)
                successResult.postValue(Unit)
            } catch (t: Throwable) {
                errorResult.postValue(t.localizedMessage)
            }
        }
    }
}