package com.example.movieviewer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieviewer.repositories.LoginRepository

class LoginTestViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
//        val result = loginRepository.login(username, password)
//
//        if (result is Result.Success) {
//            _loginResult.value = LoginResult(
//                    success = LoggedInUserView(displayName = result.data.displayName))
//        } else {
//            _loginResult.value = LoginResult(error = R.string.login_failed)
//        }
    }
}

data class LoginResult(val success: LoggedInUserView? = null, val error: Int? = null)
data class LoggedInUserView(val displayName: String
        //... other data fields that may be accessible to the UI
)