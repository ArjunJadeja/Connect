package com.arjun.connect.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arjun.connect.R
import com.arjun.connect.data.User
import com.arjun.connect.databinding.FragmentAddUsersBinding
import com.arjun.connect.util.IAddUserAdapter
import com.arjun.connect.util.SearchUsersAdapter
import com.arjun.connect.viewmodel.ConnectionsViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class AddUsersFragment : Fragment(R.layout.fragment_add_users), IAddUserAdapter,
    SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentAddUsersBinding

    private val connectionsViewModel: ConnectionsViewModel by activityViewModels()

    private lateinit var adapter: SearchUsersAdapter
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
        binding = FragmentAddUsersBinding.bind(view)

        binding.usersSearchView.setOnQueryTextListener(this)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (adapterReady) {
            adapter.stopListening()
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) searchUser(newText)
        if (newText?.trim()?.isEmpty() == true) {
            if (adapterReady) {
                adapter.stopListening()
            }
            hideUsersListLayout()
        }
        return true
    }

    private fun searchUser(username: String) {
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<User>()
            .setQuery(connectionsViewModel.searchUserQuery(username), User::class.java).build()

        adapter = SearchUsersAdapter(recyclerViewOptions, this@AddUsersFragment)
        binding.searchUsersRecyclerView.adapter = adapter
        binding.searchUsersRecyclerView.layoutManager = LinearLayoutManager(context)

        adapterReady = true
        adapter.startListening()

        showUsersListLayout()
    }

    override fun onAddClicked(userId: String) {
        connectionsViewModel.addUser(userId)
        Toast.makeText(context, "adding user...", Toast.LENGTH_SHORT).show()
    }

    private fun showUsersListLayout() {
        binding.searchUsersRecyclerView.visibility = View.VISIBLE
        binding.searchUsersImageView.visibility = View.GONE
    }

    private fun hideUsersListLayout() {
        binding.searchUsersRecyclerView.visibility = View.GONE
        binding.searchUsersImageView.visibility = View.VISIBLE
    }
}