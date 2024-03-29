package com.example.movieviewer.activities.login

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.movieviewer.MainActivity
import com.example.movieviewer.MovieViewerApplication
import com.example.movieviewer.R
import com.example.movieviewer.activities.BoundBaseActivity
import com.example.movieviewer.databinding.ActivityCreateAccountBinding
import com.example.movieviewer.longToast
import com.example.movieviewer.viewModels.CreateAccountViewModel
import com.example.movieviewer.viewModels.ViewModelFactory
import javax.inject.Inject

class CreateAccountActivity : BoundBaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: CreateAccountViewModel

    private lateinit var binding: ActivityCreateAccountBinding

    override fun injectActivity() {
        MovieViewerApplication[this].getAppComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this,
                viewModelFactory)[CreateAccountActivity::class.java.name, CreateAccountViewModel::class.java]
        setContentView(binding.root)
        initObservers()

        binding.apply {
            toolbar.setNavigationOnClickListener {
                finish()
            }
            swipeRefreshLayout.isEnabled = false
            registerBtn.setOnClickListener {
                viewModel.createUser(
                        nameInput.text?.trim().toString(),
                        usernameInput.text?.trim().toString(),
                        passwordInput.text?.trim().toString(),
                )
            }

            Glide.with(root)
                .load(R.mipmap.ic_launcher)
                .circleCrop()
                .into(image)
        }
    }

    private fun initObservers() {
        viewModel.apply {
            successResult.observe(this@CreateAccountActivity) {
                startActivity(Intent(this@CreateAccountActivity, MainActivity::class.java))
                finish()
            }
            errorResult.observe(this@CreateAccountActivity) {
                longToast(it)
            }
            isRefreshingResult.observe(this@CreateAccountActivity) {
                binding.swipeRefreshLayout.isRefreshing = it
                binding.registerBtn.isEnabled = !it
            }
            createAccountFormState.observe(this@CreateAccountActivity) {
                if (it.nameError != null) {
                    binding.nameInput.error = getString(it.nameError)
                }
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