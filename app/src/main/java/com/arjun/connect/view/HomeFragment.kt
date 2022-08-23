package com.arjun.connect.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arjun.connect.R
import com.arjun.connect.data.User
import com.arjun.connect.databinding.FragmentHomeBinding
import com.arjun.connect.util.AddedUsersAdapter
import com.arjun.connect.util.IRemoveUserAdapter
import com.arjun.connect.viewmodel.ConnectionsViewModel
import com.arjun.connect.viewmodel.NetworkViewModel
import com.arjun.connect.viewmodel.StatusViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "Intent Status"

class HomeFragment : Fragment(R.layout.fragment_home), IRemoveUserAdapter {

    private lateinit var binding: FragmentHomeBinding

    private val statusViewModel: StatusViewModel by activityViewModels()
    private val networkViewModel: NetworkViewModel by activityViewModels()
    private val connectionsViewModel: ConnectionsViewModel by activityViewModels()

    private lateinit var userNamesList: MutableList<String>

    private lateinit var adapter: AddedUsersAdapter
    private var adapterReady = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (adapterReady) {
            adapter.startListening()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        loadAddedUsersList()

        CoroutineScope(Dispatchers.Main).launch {
            binding.myStatusTextView.text = statusViewModel.getStatus()
        }

        networkViewModel.isNetworkConnected.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.noInternetTextView.visibility = View.GONE
            } else {
                binding.noInternetTextView.visibility = View.VISIBLE
            }
        }

        binding.changeStatusButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_updateStatusFragment)
        }

        binding.addUsersButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addUsersFragment)
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_user -> {
                    findNavController().navigate(R.id.action_homeFragment_to_addUsersFragment)
                    true
                }
                R.id.profile -> {
                    findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
                    true
                }
                R.id.sign_out -> {
                    AuthUI.getInstance().signOut(requireContext())
                    findNavController().navigate(R.id.action_homeFragment_to_signInFragment)
                    true
                }
                R.id.app_info -> {
                    findNavController().navigate(R.id.action_homeFragment_to_appInfoFragment)
                    true
                }
                R.id.exit_app -> {
                    finishAffinity(requireActivity())
                    true
                }
                else -> false
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finishAffinity(requireActivity())
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (adapterReady) {
            adapter.stopListening()
        }
    }

    private fun loadAddedUsersList() {
        CoroutineScope(Dispatchers.IO).launch {
            userNamesList = connectionsViewModel.getAddedUsers()

            withContext(Dispatchers.Main) {
                if (userNamesList.isNotEmpty()) {
                    val recyclerViewOptions =
                        FirestoreRecyclerOptions.Builder<User>().setQuery(
                            connectionsViewModel.addedUsersQuery(userNamesList),
                            User::class.java
                        ).build()

                    adapter = AddedUsersAdapter(recyclerViewOptions, this@HomeFragment)
                    binding.addedUsersRecyclerView.adapter = adapter
                    binding.addedUsersRecyclerView.layoutManager = LinearLayoutManager(context)

                    adapterReady = true
                    adapter.startListening()

                    hideEmptyListLayout()

                } else {
                    showEmptyListLayout()
                }
            }
        }
    }

    private fun hideEmptyListLayout() {
        binding.emptyListLayout.visibility = View.GONE
        binding.addedUsersRecyclerView.visibility = View.VISIBLE
    }

    private fun showEmptyListLayout() {
        binding.emptyListLayout.visibility = View.VISIBLE
        binding.addedUsersRecyclerView.visibility = View.GONE
    }

    override fun onRemoveClicked(userId: String) {
        connectionsViewModel.removeUser(userId)
        Toast.makeText(context, "removing user...", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            loadAddedUsersList()
        }, 1000)
    }

    override fun linkedInProfile(linkedInUserName: String) {
        val webIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/$linkedInUserName/"))
        try {
            startActivity(webIntent)
        } catch (ex: ActivityNotFoundException) {
            Log.d(TAG, ex.toString())
        }
    }

    override fun twitterProfile(twitterUserName: String) {
        val webIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/$twitterUserName"))
        try {
            startActivity(webIntent)
        } catch (ex: ActivityNotFoundException) {
            Log.d(TAG, ex.toString())
        }
    }

    override fun instagramProfile(instagramUserName: String) {
        val webIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/$instagramUserName"))
        try {
            startActivity(webIntent)
        } catch (ex: ActivityNotFoundException) {
            Log.d(TAG, ex.toString())
        }
    }

    override fun whatsAppProfile(whatsappNumber: String) {
        val countryCode = +91
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://api.whatsapp.com/send?phone=$countryCode$whatsappNumber")
        )
        try {
            startActivity(webIntent)
        } catch (ex: ActivityNotFoundException) {
            Log.d(TAG, ex.toString())
        }
    }

    override fun phoneProfile(phoneNumber: String) {
        val countryCode = +91
        val webIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$countryCode$phoneNumber"))
        try {
            startActivity(webIntent)
        } catch (ex: ActivityNotFoundException) {
            Log.d(TAG, ex.toString())
        }
    }
}