package com.arjun.connect.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arjun.connect.R
import com.arjun.connect.databinding.FragmentAppInfoBinding

class AppInfoFragment : Fragment(R.layout.fragment_app_info) {

    private lateinit var binding: FragmentAppInfoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAppInfoBinding.bind(view)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}