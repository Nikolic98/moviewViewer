package com.example.movieviewer.activities.login

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.movieviewer.MainActivity
import com.example.movieviewer.MovieViewerApplication
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

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        initObservers()
        binding.registerButton.setOnClickListener {
            viewModel.createUser(
                    binding.name.text.trim().toString(),
                    binding.username.text.trim().toString(),
                    binding.password.text.trim().toString(),
            )
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
        }
    }
}