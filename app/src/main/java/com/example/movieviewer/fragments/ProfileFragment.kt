package com.example.movieviewer.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.movieviewer.MovieViewerApplication
import com.example.movieviewer.activities.login.LoginActivity
import com.example.movieviewer.databinding.FragmentProfileBinding
import com.example.movieviewer.viewModels.ProfileViewModel
import com.example.movieviewer.viewModels.ViewModelFactory
import javax.inject.Inject

/**
 * @author Marko Nikolic on 18.8.23.
 */
class ProfileFragment : BoundBaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel

    override fun injectFragment() {
        MovieViewerApplication[requireActivity()].getAppComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this,
                viewModelFactory)[ProfileViewModel::class.java.name, ProfileViewModel::class.java]
        initObservers()

        binding.logoutBtn.setOnClickListener {
            viewModel.logoutUser()
        }
        return binding.root
    }

    private fun initObservers() {
        viewModel.apply {
            text.observe(viewLifecycleOwner) {
                binding.text.text = it
            }
            successResult.observe(viewLifecycleOwner) {
                startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}