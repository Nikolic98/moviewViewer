package com.example.movieviewer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

/**
 * @author Marko Nikolic on 16.8.23.
 */
class MainViewModel @Inject constructor() : ViewModel() {

    private lateinit var auth: FirebaseAuth

    val successResult by lazy { MutableLiveData<Unit>() }

    fun logoutUser() {
        auth = Firebase.auth
        auth.signOut()
        successResult.postValue(Unit)
    }
}