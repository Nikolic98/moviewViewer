package com.example.movieviewer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieviewer.R
import com.example.movieviewer.repositories.UserRepository
import com.example.movieviewer.utils.TextValidationHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Marko Nikolic on 17.8.23.
 */
class CreateAccountViewModel @Inject constructor(
        private val loginRepository: UserRepository) : ViewModel() {

    private val _createAccountFormState = MutableLiveData<CreateAccountFormState>()
    val createAccountFormState: LiveData<CreateAccountFormState> = _createAccountFormState

    val successResult by lazy { MutableLiveData<Unit>() }
    val errorResult by lazy { MutableLiveData<String>() }
    val isRefreshingResult by lazy { MutableLiveData<Boolean>() }

    fun createUser(name: String, username: String, password: String) {
        isRefreshingResult.value = true
        textValidation(name, username, password)
        if (createAccountFormState.value?.isDataValid == false) {
            isRefreshingResult.value = false
            return
        }
        viewModelScope.launch {
            loginRepository.createNewUser(name, username, password)
                .onSuccess {
                    loginRepository.createNewUser(name, username, password)
                    successResult.postValue(Unit)
                    isRefreshingResult.postValue(false)
                }
                .onFailure {
                    errorResult.postValue(it.localizedMessage)
                    isRefreshingResult.postValue(false)
                }
        }
    }

    private fun textValidation(name: String, username: String, password: String) {
        var nameError: Int? = null
        var usernameError: Int? = null
        var passwordError: Int? = null
        var isDataValid = true
        if (name.isBlank()) {
            isDataValid = false
            nameError = R.string.invalid_empty_field
        }
        if (!TextValidationHelper.isUsernameValid(username)) {
            isDataValid = false
            usernameError = R.string.invalid_username
        }

        if (!TextValidationHelper.isPasswordValid(password)) {
            isDataValid = false
            passwordError = R.string.invalid_password
        }

        _createAccountFormState.value = CreateAccountFormState(nameError, usernameError,
                passwordError,
                isDataValid)
    }
}

data class CreateAccountFormState(
        val nameError: Int? = null,
        val usernameError: Int? = null,
        val passwordError: Int? = null,
        val isDataValid: Boolean = false)