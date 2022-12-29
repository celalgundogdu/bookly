package com.example.bookly

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.bookly.databinding.FragmentProfileBinding
import com.example.bookly.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        binding.textViewFullName.text = "Welcome, ".plus(viewModel.user.value?.displayName)
        viewModel.user.observe(viewLifecycleOwner) {
            binding.textViewFullName.text = "Welcome, ".plus(it.displayName)
        }

        binding.buttonSave.setOnClickListener{
            val newName = binding.editTextNewName.text.toString()
            val newPassword = binding.editTextNewPassword.text.toString()
            if(checkNewFields()) {
                update(newName, newPassword)
            }
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ProfileViewModel by viewModels()
        this.viewModel = tempViewModel
    }

    fun update(newName: String, newPassword: String) {
        viewModel.updateUser(newName, newPassword)
    }

    private fun checkNewFields(): Boolean {
        var isValid = true
        if(binding.editTextNewName.text.toString().isEmpty()) {
            binding.editTextNewName.error = "Name can not be empty"
            isValid = false
        }
        if(binding.editTextNewPassword.text.toString().isEmpty()) {
            binding.editTextNewPassword.error = "Password must be at least 6 characters"
            isValid = false
        }
        binding.editTextNewPassword.doOnTextChanged{text, start, before, count ->
            if (text!!.length < 6) {
                binding.editTextNewPassword.error = "Password must be at least 6 characters"
                isValid = false
            } else {
                binding.editTextNewPassword.error = null
                isValid = true
            }
        }
        return isValid
    }
}