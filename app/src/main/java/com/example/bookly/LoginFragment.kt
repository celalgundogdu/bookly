package com.example.bookly

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.bookly.databinding.FragmentLoginBinding
import com.example.bookly.viewmodel.LoginViewModel
import com.example.bookly.model.MyResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : Fragment() {
    
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.loginObj = this

        // login
        binding.buttonLogin.setOnClickListener{
            if (checkLoginFields()) {
                val email = binding.editTextEmail.text.toString().trim()
                val password = binding.editTextPassword.text.toString()
                var result: MyResult?
                lifecycleScope.launch(Dispatchers.IO) {
                    result = login(email, password)
                    withContext(Dispatchers.Main) {
                        if (result?.code == 200) {
                            startActivity(Intent(activity, UserActivity::class.java))
                            activity?.finish()
                        } else {
                            Toast.makeText(activity, result?.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        // go to register fragment
        binding.textViewGoToRegister.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: LoginViewModel by viewModels()
        this.viewModel = tempViewModel
    }

    private suspend fun login(email: String, password: String): MyResult? {
        return viewModel.login(email, password)
    }

    fun resetPassword() {
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_forgot_password, null)
        val editTextEmail = dialogLayout.findViewById<EditText>(R.id.dialog_email)
        val dialog = activity?.let { it1 -> AlertDialog.Builder(it1) }
        dialog?.setView(dialogLayout)
        dialog?.setTitle("Password Reset")
        dialog?.setIcon(R.drawable.ic_reset)
        dialog?.setPositiveButton("Send") { _, _ ->
            if (checkEmail(editTextEmail)) {
                viewModel.resetPassword(editTextEmail.text.toString())
            }
        }
        dialog?.setNegativeButton("Close"){ _, _ -> }
        dialog?.create()?.show()
    }

    private fun checkLoginFields(): Boolean {
        var isValid = true
        if(binding.editTextEmail.text.toString().isEmpty()) {
            binding.textInputLayoutEmail.error = "Invalid email"
            isValid = false
        }
        if(binding.editTextPassword.text.toString().isEmpty()) {
            binding.textInputLayoutPassword.error = "Password must be at least 6 characters"
            isValid = false
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
                binding.textInputLayoutPassword.error = "Password must be at least 6 characters"
                isValid = false
            } else {
                binding.textInputLayoutPassword.error = null
                isValid = true
            }
        }
        return isValid
    }

    private fun checkEmail(email: EditText): Boolean {
        var isValid = true
        if(email.text.toString().isEmpty()) {
            binding.textInputLayoutPassword.error = "Password must be at least 6 characters"
            isValid = false
        }
        email.doOnTextChanged{text, start, before, count ->
            if (!text?.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() }!!) {
                binding.textInputLayoutEmail.error = "Invalid email"
                isValid = false
            } else {
                binding.textInputLayoutEmail.error = null
                isValid = true
            }
        }
        return isValid
    }

}