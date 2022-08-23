package com.arjun.connect.view

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.arjun.connect.R
import com.arjun.connect.databinding.FragmentSignInBinding
import com.arjun.connect.viewmodel.RegistrationViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private lateinit var binding: FragmentSignInBinding
    private val registrationViewModel: RegistrationViewModel by activityViewModels()

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInBinding.bind(view)

        // Authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.ic_connect_logo)
            .setTheme(R.style.Theme_Sign_In)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        try {
            if (result.resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                CoroutineScope(Dispatchers.Main).launch {
                    if (registrationViewModel.userRegistered(user!!.uid)) {
                        findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                    } else {
                        registrationViewModel.registerUser(
                            user.uid,
                            user.photoUrl.toString(),
                            user.displayName.toString()
                        )
                        findNavController().navigate(R.id.action_signInFragment_to_updateProfileFragment)
                    }
                }
            } else {
                Toast.makeText(context, "Failed to SignIn", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to signIn", Toast.LENGTH_SHORT).show()
        }
    }
}