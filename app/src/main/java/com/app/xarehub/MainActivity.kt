package com.app.xarehub

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.app.xarehub.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import tech.cherri.tpdirect.api.TPDServerType
import tech.cherri.tpdirect.api.TPDSetup

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    enum class BottomNavItem {
        HOME,
        SEARCH,
        KEY,
        PROFILE
    }

    private var onlySetBottomItem = false

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appId = 0
        val appKey = ""

        val serverType = if (BuildConfig.DEBUG) TPDServerType.Sandbox else TPDServerType.Production
        TPDSetup.initInstance(
            this,
            appId,
            appKey,
            serverType)

        setupNavigationComponents()
    }

    private fun setupNavigationComponents() {

        binding.apply {

            navController = findNavController(R.id.nav_host_fragment_activity_main)

            val navGraph = navController.navInflater.inflate(R.navigation.mobile_navigation)
            navGraph.startDestination = R.id.navigation_home

            navController.graph = navGraph

            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.navigation_home -> showBottomNavView()
                    R.id.navigation_search -> showBottomNavView()
                    R.id.navigation_key -> showBottomNavView()
                    R.id.navigation_my_profile -> showBottomNavView()
                    else -> hideBottomNavView()
                }
            }

            bottomNavView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.navigation_my_profile -> {
                        if (!onlySetBottomItem) {
                            navController.navigate(R.id.action_global_navigation_my_profile)
                        }
                        onlySetBottomItem = false
                        true
                    }
                    R.id.navigation_key -> {
                        if (!onlySetBottomItem) {
                            navController.navigate(R.id.action_global_navigation_key)
                        }
                        onlySetBottomItem = false
                        true
                    }

                    R.id.navigation_search -> {
                        if (!onlySetBottomItem) {
                            navController.navigate(R.id.action_global_navigation_search)
                        }
                        onlySetBottomItem = false
                        true
                    }

                    R.id.navigation_home -> {
                        if (!onlySetBottomItem) {
                            navController.navigate(R.id.action_global_navigation_home)
                        }
                        onlySetBottomItem = false
                        true
                    }

                    else -> {
                        navController.navigate(it.itemId)
                        onlySetBottomItem = false
                        true
                    }
                }
            }
        }
    }

    fun setBottomNavSelected(item: BottomNavItem) {
        onlySetBottomItem = true
        binding.bottomNavView.selectedItemId = when (item) {
            BottomNavItem.HOME -> R.id.navigation_home
            BottomNavItem.SEARCH -> R.id.navigation_search
            BottomNavItem.KEY -> R.id.navigation_key
            BottomNavItem.PROFILE -> R.id.navigation_my_profile
        }
    }

    private fun showBottomNavView() {
        binding.bottomNavView.visibility = View.VISIBLE
    }

    private fun hideBottomNavView() {
        binding.bottomNavView.visibility = View.GONE
    }
}