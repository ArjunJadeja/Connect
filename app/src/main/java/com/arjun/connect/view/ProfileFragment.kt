package com.arjun.connect.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.arjun.connect.R
import com.arjun.connect.data.User
import com.arjun.connect.databinding.FragmentProfileBinding
import com.arjun.connect.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        CoroutineScope(Dispatchers.Main).launch {
            val user = profileViewModel.getUserInfo().toObject(User::class.java)
            user?.apply {
                Glide.with(this@ProfileFragment).load(profileImageUrl)
                    .into(binding.profileImage)
                binding.nameText.setText(name)
                binding.usernameText.setText(username)
                binding.statusText.setText(status)
                if (linkedInUserName != "") {
                    binding.linkedInUsernameText.setText(linkedInUserName)
                }
                if (twitterUserName != "") {
                    binding.twitterUsernameText.setText(twitterUserName)
                }
                if (instagramUserName != "") {
                    binding.instagramUsernameText.setText(instagramUserName)
                }
                if (whatsAppNumber != "") {
                    binding.whatsappNumberText.setText(whatsAppNumber)
                }

            }
        }

        binding.nameText.error = null
        binding.usernameText.error = null
        binding.statusText.error = null
        binding.linkedInUsernameText.error = null
        binding.twitterUsernameText.error = null
        binding.instagramUsernameText.error = null
        binding.whatsappNumberText.error = null

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.editProfileButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_updateProfileFragment)
        }
    }
}