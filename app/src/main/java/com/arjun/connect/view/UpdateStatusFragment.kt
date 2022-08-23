package com.arjun.connect.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.arjun.connect.R
import com.arjun.connect.databinding.FragmentUpdateStatusBinding
import com.arjun.connect.viewmodel.StatusViewModel

class UpdateStatusFragment : Fragment(R.layout.fragment_update_status) {

    private lateinit var binding: FragmentUpdateStatusBinding
    private val statusViewModel: StatusViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpdateStatusBinding.bind(view)

        showSoftKeyboard(binding.updatedStatusEditText)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.update_status -> {
                    val status = binding.updatedStatusEditText.text.toString().trim()
                    if (status.isNotEmpty()) {
                        statusViewModel.updateStatus(status)
                        findNavController().navigateUp()
                    } else {
                        Toast.makeText(context, "Nothing to save", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                else -> false
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}