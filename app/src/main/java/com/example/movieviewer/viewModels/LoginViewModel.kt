package com.example.movieviewer.viewModels

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieviewer.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

/**
 * @author Marko Nikolic on 17.8.23.
 */
class LoginViewModel @Inject constructor() : ViewModel() {

    private lateinit var auth: FirebaseAuth

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    val successResult by lazy { MutableLiveData<Unit>() }
    val errorResult by lazy { MutableLiveData<String>() }

    fun loginUser(username: String, password: String) {
        usernameAndPasswordValidation(username, password)
        if (loginFormState.value?.isDataValid == false) {
            return
        }
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
//                    val user = auth.currentUser
//                    updateUI(user)
                    successResult.postValue(Unit)
                } else {
                    errorResult.postValue(task.exception?.message)
                }
            }
    }

    private fun usernameAndPasswordValidation(username: String, password: String) {
        var usernameError: Int? = null
        var passwordError: Int? = null
        var isDataValid = true
        if (!isUsernameValid(username)) {
            isDataValid = false
            usernameError = R.string.invalid_username
        }

        if (!isPasswordValid(password)) {
            isDataValid = false
            passwordError = R.string.invalid_password
        }

        _loginForm.value = LoginFormState(usernameError, passwordError, isDataValid)
    }

    // A placeholder username validation check
    private fun isUsernameValid(username: String): Boolean {
        return if (username.isNotBlank()) {
            if (username.contains('@')) {
                Patterns.EMAIL_ADDRESS.matcher(username).matches()
            } else {
                false
            }
        } else {
            false
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String) = password.length > 5
}

data class LoginFormState(
        val usernameError: Int? = null,
        val passwordError: Int? = null,
        val isDataValid: Boolean = false)