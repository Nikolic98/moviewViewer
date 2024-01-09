package com.example.movieviewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.movieviewer.activities.BoundBaseActivity
import com.example.movieviewer.databinding.ActivityMainBinding
import com.example.movieviewer.fragments.HomeFragment
import com.example.movieviewer.fragments.ProfileFragment
import com.example.movieviewer.fragments.SearchFragment
import com.example.movieviewer.fragments.WatchListFragment
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

        val homeFragment = HomeFragment()
        val searchFragment = SearchFragment()
        val watchListFragment = WatchListFragment()
        val profileFragment = ProfileFragment()

        // initialPage
        setFragment(homeFragment)

        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> setFragment(homeFragment)
                R.id.navigation_search -> setFragment(searchFragment)
                R.id.navigation_watchlist -> setFragment(watchListFragment)
                R.id.navigation_profile -> setFragment(profileFragment)
            }
            true
        }
        setContentView(binding.root)
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // Hide all other fragments
        for (existingFragment in supportFragmentManager.fragments) {
            fragmentTransaction.hide(existingFragment)
        }

        // Check if the fragment is added, if not, add it
        if (!fragment.isAdded) {
            fragmentTransaction.add(binding.container.id, fragment)
        }

        // Show the selected fragment
        fragmentTransaction.show(fragment).commit()
    }
}