package com.example.movieviewer.activities.login

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.movieviewer.MainActivity
import com.example.movieviewer.MovieViewerApplication
import com.example.movieviewer.activities.BoundBaseActivity
import com.example.movieviewer.databinding.ActivityLoginBinding
import com.example.movieviewer.longToast
import com.example.movieviewer.viewModels.LoginViewModel
import com.example.movieviewer.viewModels.ViewModelFactory
import javax.inject.Inject

class LoginActivity : BoundBaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: LoginViewModel

    private lateinit var binding: ActivityLoginBinding

    override fun injectActivity() {
        MovieViewerApplication[this].getAppComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this,
                viewModelFactory)[LoginActivity::class.java.name, LoginViewModel::class.java]
        setContentView(binding.root)

        initObservers()
        binding.apply {
            loginBtn.setOnClickListener {
                viewModel.loginUser(
                        username.text.trim().toString(),
                        password.text.trim().toString(),
                )
            }
            registerBtn.setOnClickListener {
                startActivity(Intent(this@LoginActivity, CreateAccountActivity::class.java))
            }
        }
    }

    private fun initObservers() {
        viewModel.apply {
            successResult.observe(this@LoginActivity) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
            errorResult.observe(this@LoginActivity) {
                longToast(it)
            }
        }
    }
//
//    fun replaceFragment(fragment: Fragment) {
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(binding.container.id, fragment)
//        fragmentTransaction.commit()
//    }
}