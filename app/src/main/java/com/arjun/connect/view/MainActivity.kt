package com.arjun.connect.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.arjun.connect.R
import com.arjun.connect.databinding.ActivityMainBinding
import com.arjun.connect.util.NetworkConnected
import com.arjun.connect.viewmodel.NetworkViewModel
import com.arjun.connect.viewmodel.RegistrationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val networkViewModel: NetworkViewModel by viewModels()
    private val registrationViewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Connect)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.navigation_fragment) as NavHostFragment?
        val navController = navHost!!.navController
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.app_navigation)

        CoroutineScope(Dispatchers.Main).launch {
            if (registrationViewModel.currentUser == null) {
                graph.setStartDestination(R.id.signInFragment)
            } else {
                if (registrationViewModel.profileSet()) {
                    graph.setStartDestination(R.id.homeFragment)
                } else {
                    graph.setStartDestination(R.id.updateProfileFragment)
                }
            }
            navController.graph = graph
        }

        val networkConnected = NetworkConnected(this)
        networkConnected.observe(this) {
            networkViewModel.getNetwork(it)
        }
    }
}