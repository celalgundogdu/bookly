package com.example.bookly.viewmodel

import androidx.lifecycle.ViewModel
import com.example.bookly.model.MyResult
import com.example.bookly.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(var authRepository: AuthRepository): ViewModel() {

    //private val authRepository = AuthRepository()

    suspend fun login(email: String, password: String): MyResult? {
        return authRepository.login(email, password)
    }

    fun resetPassword(email: String) {
        return authRepository.resetPassword(email)
    }
}