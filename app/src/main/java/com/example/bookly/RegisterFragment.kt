package com.example.bookly

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.bookly.databinding.FragmentRegisterBinding
import com.example.bookly.model.MyResult
import com.example.bookly.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.registerObj = this

        // register
        binding.buttonRegister.setOnClickListener{
            if (checkRegisterFields()) {
                val fullName = binding.editTextFullName.text.toString().trim()
                val email = binding.editTextEmail.text.toString().trim()
                val password = binding.editTextPassword.text.toString()
                var result: MyResult?
                lifecycleScope.launch(Dispatchers.IO) {
                    result = register(fullName, email, password)
                    withContext(Dispatchers.Main){
                        if(result?.code == 200){
                            Navigation.findNavController(it).navigate(R.id.action_registerFragment_to_loginFragment)
                        } else {
                            Toast.makeText(activity, result?.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        // go to login fragment
        binding.textViewGoToLogin.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_registerFragment_to_loginFragment)
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: RegisterViewModel by viewModels()
        this.viewModel = tempViewModel
    }

    private suspend fun register(fullName: String, email: String, password: String): MyResult? {
        return viewModel.register(fullName, email, password)
    }

    private fun checkRegisterFields(): Boolean {
        var isValid = true
        if(binding.editTextFullName.text.toString().isEmpty()) {
            binding.textInputLayoutFullName.error = "Name can not be empty"
            isValid = false
        }
        if(binding.editTextEmail.text.toString().isEmpty()) {
            binding.textInputLayoutEmail.error = "Email can not be empty"
            isValid = false
        }
        if(binding.editTextPassword.text.toString().isEmpty()) {
            binding.textInputLayoutPassword.error = "Password must be at least 6 characters"
            isValid = false
        }
        binding.editTextFullName.doOnTextChanged{text, start, before, count ->
            if (text!!.trim().isEmpty()) {
                binding.textInputLayoutFullName.error = "This is required field"
                isValid = false
            } else {
                binding.textInputLayoutFullName.error = null
                isValid = true
            }
        }
        binding.editTextEmail.doOnTextChanged{text, start, before, count ->
            if (!text?.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() }!!) {
                binding.textInputLayoutEmail.error = "Invalid email"
                isValid = false
            } else {
                binding.textInputLayoutEmail.error = null
                isValid = true
            }
        }
        binding.editTextPassword.doOnTextChanged{text, start, before, count ->
            if (text!!.length < 6) {
                binding.textInputLayoutPassword.error = "Password can not be empty"
                isValid = false
            } else {
                binding.textInputLayoutPassword.error = null
                isValid = true
            }
        }
        return isValid
    }
}