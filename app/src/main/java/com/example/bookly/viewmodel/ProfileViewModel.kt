package com.example.bookly.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookly.model.UserBook
import com.example.bookly.repo.UserRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(var userRepository: UserRepository): ViewModel() {

    //private val userRepository = UserRepository()
    var user =  MutableLiveData<FirebaseUser>()

    init {
        user = userRepository.getUser()
    }

    fun updateUser(newName: String, newPassword: String) {
        userRepository.updateUser(newName, newPassword)
    }
}