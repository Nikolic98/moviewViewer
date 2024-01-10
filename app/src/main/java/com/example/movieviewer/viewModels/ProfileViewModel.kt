package com.example.movieviewer.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieviewer.R
import com.example.movieviewer.repositories.UserRepository
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Marko Nikolic on 18.8.23.
 */
class ProfileViewModel @Inject constructor(
        private val userRepository: UserRepository) : ViewModel() {

    private val _userData = MutableLiveData<UserData>()
    val userData: LiveData<UserData> = _userData

    val logoutActionResult by lazy { MutableLiveData<Unit>() }
    val errorResult by lazy { MutableLiveData<String>() }
    val isRefreshingResult by lazy { MutableLiveData<Boolean>() }

    fun getCurrentUser(context: Context) {
        isRefreshingResult.value = true
        viewModelScope.launch {
            userRepository.getCurrentUser().onSuccess {
                _userData.postValue(
                        UserData("${context.getString(R.string.account_name)} ${it.displayName}",
                                "${context.getString(R.string.registered_email)} ${it.email}"))
                isRefreshingResult.postValue(false)
            }.onFailure {
                errorResult.postValue(it.localizedMessage)
                isRefreshingResult.postValue(false)
            }
        }
    }

    fun logoutUser() {
        userRepository.logout()
        logoutActionResult.postValue(Unit)
    }

    fun deleteUser() {
        isRefreshingResult.value = true
        viewModelScope.launch {
            userRepository.deleteUser().onSuccess {
                logoutActionResult.postValue(Unit)
                isRefreshingResult.postValue(false)
            }.onFailure {
                errorResult.postValue(it.localizedMessage)
                isRefreshingResult.postValue(false)
            }
        }
    }
}

 data class UserData(val name: String, val email: String)