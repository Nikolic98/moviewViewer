package com.example.movieviewer.activities.login

import android.app.Activity
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.movieviewer.R
import com.example.movieviewer.databinding.ActivityTestLoginBinding
import com.example.movieviewer.viewModels.LoggedInUserView
import com.example.movieviewer.viewModels.LoginTestViewModel

class LoginTestActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginTestViewModel
    private lateinit var binding: ActivityTestLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTestLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading

//        loginViewModel = ViewModelProvider(this, LoginViewModelFactory()).get(
//                    LoginTestViewModel::class.java)

        loginViewModel.loginResult.observe(this@LoginTestActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        password.apply {
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> loginViewModel.login(username.text.toString(),
                            password.text.toString())
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(applicationContext, "$welcome $displayName", Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}