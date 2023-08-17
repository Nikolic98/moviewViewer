package com.example.movieviewer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Marko Nikolic on 17.8.23.
 */
class CreateAccountViewModel @Inject constructor() : ViewModel() {

    private lateinit var auth: FirebaseAuth

    val successResult by lazy { MutableLiveData<Unit>() }
    val errorResult by lazy { MutableLiveData<String>() }

    fun createUser(username: String, password: String) {
        auth = Firebase.auth
        viewModelScope.launch {}

        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "createUserWithEmail:success")
//                    val user = auth.currentUser
//                    updateUI(user)
                    successResult.postValue(Unit)
                } else {
                    val input = task.exception?.message!!
                    val startIndex = input.indexOf('[')
                    val endIndex = input.indexOf(']')
                    val errorMessage = if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                        input.substring(startIndex + 1, endIndex)
                    } else {
                        input
                    }
                    errorResult.postValue(errorMessage)
                }
            }
    }
}