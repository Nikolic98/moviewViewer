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

        initObservers()

        // initialPage
        setFragment(HomeFragment())

        binding.navView.setOnItemSelectedListener {
            // By using switch we can easily get
            // the selected fragment
            // by using there id.
            // By using switch we can easily get
            // the selected fragment
            // by using there id.
            var selectedFragment: Fragment? = null
            when (it.itemId) {
                R.id.navigation_home -> {
                    selectedFragment = HomeFragment()
                }

                R.id.navigation_search -> {
                    selectedFragment = SearchFragment()
                }

                R.id.navigation_watchlist -> {
                    selectedFragment = WatchListFragment()
                }

                R.id.navigation_profile -> {
                    selectedFragment = ProfileFragment()
                }
            }
            if (selectedFragment != null) {
                setFragment(selectedFragment)
            }
            true
        }

        setContentView(binding.root)
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(binding.container.id,
                fragment).commit()
    }

    private fun initObservers() {
    }
}