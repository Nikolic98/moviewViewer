package com.example.movieviewer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

/**
 * @author Marko Nikolic on 17.8.23.
 */
class LoginViewModel @Inject constructor() : ViewModel() {

    private lateinit var auth: FirebaseAuth

    val successResult by lazy { MutableLiveData<Unit>() }
    val errorResult by lazy { MutableLiveData<String>() }

    fun loginUser(username: String, password: String) {
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
}