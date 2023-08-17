package com.example.movieviewer

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.movieviewer.activities.BoundBaseActivity
import com.example.movieviewer.activities.login.LoginActivity
import com.example.movieviewer.databinding.ActivityMainBinding
import com.example.movieviewer.viewModels.MainViewModel
import com.example.movieviewer.viewModels.ViewModelFactory
import javax.inject.Inject

class MainActivity : BoundBaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding

    override fun injectActivity() {
        MovieViewerApplication[this].getAppComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this,
                viewModelFactory)[MainActivity::class.java.name, MainViewModel::class.java]

        initObservers()
        binding.logoutBtn.setOnClickListener {
            viewModel.logoutUser()
        }

        setContentView(binding.root)
    }

    private fun initObservers() {
        viewModel.apply {
            successResult.observe(this@MainActivity) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}