package com.example.movieviewer.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.movieviewer.MovieViewerApplication
import com.example.movieviewer.R
import com.example.movieviewer.activities.login.LoginActivity
import com.example.movieviewer.databinding.FragmentProfileBinding
import com.example.movieviewer.longToast
import com.example.movieviewer.viewModels.ProfileViewModel
import com.example.movieviewer.viewModels.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        viewModel.getCurrentUser(requireActivity())

        binding.logoutBtn.setOnClickListener {
            viewModel.logoutUser()
        }
        binding.deleteBtn.setOnClickListener {
            MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.delete_account)
                .setMessage(R.string.delete_account_message)
                .setPositiveButton(R.string.yes) { _, _ ->
                    viewModel.deleteUser()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        return binding.root
    }

    private fun initObservers() {
        viewModel.apply {
            userData.observe(viewLifecycleOwner) {
                binding.nameText.text = it.name
                binding.emailText.text = it.email
            }
            logoutActionResult.observe(viewLifecycleOwner) {
                startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            }
            isRefreshingResult.observe(viewLifecycleOwner) {
                binding.swipeRefreshLayout.isRefreshing = it
            }
            errorResult.observe(viewLifecycleOwner) {
                activity?.longToast(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}