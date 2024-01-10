package com.example.movieviewer.activities.login

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.movieviewer.MainActivity
import com.example.movieviewer.MovieViewerApplication
import com.example.movieviewer.R
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
                        usernameInput.text?.trim().toString(),
                        passwordInput.text?.trim().toString(),
                )
            }
            registerBtn.setOnClickListener {
                startActivity(Intent(this@LoginActivity, CreateAccountActivity::class.java))
            }
            Glide.with(root)
                .load(R.mipmap.ic_launcher)
                .circleCrop()
                .into(image)
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
            loginFormState.observe(this@LoginActivity) {
                if (it.usernameError != null) {
                    binding.usernameInput.error = getString(it.usernameError)
                }
                if (it.passwordError != null) {
                    binding.passwordInput.error = getString(it.passwordError)
                }
            }
        }
    }
}