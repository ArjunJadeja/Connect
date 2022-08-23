package com.arjun.connect.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.arjun.connect.R
import com.arjun.connect.data.User
import com.arjun.connect.databinding.FragmentUpdateProfileBinding
import com.arjun.connect.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UpdateProfileFragment : Fragment(R.layout.fragment_update_profile) {

    private lateinit var binding: FragmentUpdateProfileBinding

    private val profileViewModel: ProfileViewModel by activityViewModels()

    private lateinit var savedUserName: String

    private lateinit var imageUri: Uri
    private var imageSelected = false

    private val getImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                imageUri = result.data?.data!!
                binding.profileImageView.setImageURI(imageUri)
                imageSelected = true
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpdateProfileBinding.bind(view)

        loadUserInfo()

        binding.changeImageButton.setOnClickListener {
            selectImage()
        }

        binding.usernameEditText.doOnTextChanged { text, _, _, _ ->
            when (usernameSyntax(text.toString().trim())) {
                "empty" -> {
                    binding.usernameInput.error = null
                }
                "invalid text" -> {
                    binding.usernameInput.error = "only lowercase, numbers and underscores allowed"
                }
                "less than 4" -> {
                    binding.usernameInput.error = "minimum 4 characters required"
                }
                "more than 16" -> {
                    binding.usernameInput.error = "maximum 16 characters allowed"
                }
                "ok" -> {
                    binding.usernameInput.error = null
                }
            }
        }

        binding.nextButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            CoroutineScope(Dispatchers.Main).launch {
                when (checkUsername(username)) {
                    "ok" -> saveProfileInfo()
                    "empty" -> Toast.makeText(context, "Enter username", Toast.LENGTH_SHORT).show()
                    "invalid" -> Toast.makeText(context, "Invalid username", Toast.LENGTH_SHORT)
                        .show()
                    "username exists" ->
                        Toast.makeText(context, "username already taken", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.saveButton.setOnClickListener {
            profileViewModel.updateSocialInfo(
                binding.linkedInUsernameEditText.text.toString().trim(),
                binding.twitterUsernameEditText.text.toString().trim(),
                binding.instagramUsernameEditText.text.toString().trim(),
                binding.whatsappNumberEditText.text.toString().trim()
            )
            Toast.makeText(
                context,
                "It takes some time to reflect updates on your profile",
                Toast.LENGTH_LONG
            ).show()
            findNavController().navigate(R.id.action_updateProfileFragment_to_homeFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    CoroutineScope(Dispatchers.Main).launch {
                        if (binding.socialInfoTextView.isVisible) {
                            hideSocialInfo()
                            showProfileInfo()
                        } else {
                            findNavController().navigateUp()
                        }
                    }
                }
            }
        )
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        getImageLauncher.launch(intent)
    }

    private suspend fun checkUsername(username: String): String {
        return when {
            username == savedUserName -> "ok"
            usernameSyntax(username) == "empty" -> "empty"
            usernameSyntax(username) != "ok" -> "invalid"
            profileViewModel.usernameExists(username) -> "username exists"
            else -> "ok"
        }
    }

    private fun usernameSyntax(username: String): String {
        return when {
            username.isEmpty() -> "empty"
            username.length < 4 -> "less than 4"
            username.length > 16 -> "more than 16"
            !username.matches("[a-z0-9_]+".toRegex()) -> "invalid text"
            else -> "ok"
        }
    }

    private fun loadUserInfo() {
        CoroutineScope(Dispatchers.Main).launch {
            val user = profileViewModel.getUserInfo().toObject(User::class.java)
            savedUserName = user?.username.toString()
            user?.apply {
                Glide.with(this@UpdateProfileFragment).load(profileImageUrl)
                    .into(binding.profileImageView)
                if (name != "") {
                    binding.nameEditText.setText(name)
                }
                binding.usernameEditText.setText(username)
                binding.statusEditText.setText(status)
                binding.linkedInUsernameEditText.setText(linkedInUserName)
                binding.twitterUsernameEditText.setText(twitterUserName)
                binding.instagramUsernameEditText.setText(instagramUserName)
                binding.whatsappNumberEditText.setText(whatsAppNumber)
            }
        }
    }

    private fun saveProfileInfo() {
        val name = binding.nameEditText.text.toString().trim()
        val username = binding.usernameEditText.text.toString().trim()
        val status = binding.statusEditText.text.toString().trim()
        if (name != "" && username != "" && status != "") {
            if (imageSelected) {
                profileViewModel.uploadImage(imageUri)
            }
            profileViewModel.updateProfileInfo(name, username, status)
            hideProfileInfo()
            showSocialInfo()
        } else {
            Toast.makeText(context, "Check input", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideProfileInfo() {
        binding.profileInfoTextView.visibility = View.GONE
        binding.updateProfileInfoScrollView.visibility = View.GONE
        binding.nextButton.visibility = View.GONE
    }

    private fun showProfileInfo() {
        binding.profileInfoTextView.visibility = View.VISIBLE
        binding.updateProfileInfoScrollView.visibility = View.VISIBLE
        binding.nextButton.visibility = View.VISIBLE
    }

    private fun showSocialInfo() {
        binding.socialInfoTextView.visibility = View.VISIBLE
        binding.updateSocialInfoScrollView.visibility = View.VISIBLE
        binding.noteTextView.visibility = View.VISIBLE
        binding.saveButton.visibility = View.VISIBLE
    }

    private fun hideSocialInfo() {
        binding.socialInfoTextView.visibility = View.GONE
        binding.updateSocialInfoScrollView.visibility = View.GONE
        binding.noteTextView.visibility = View.GONE
        binding.saveButton.visibility = View.GONE
    }
}