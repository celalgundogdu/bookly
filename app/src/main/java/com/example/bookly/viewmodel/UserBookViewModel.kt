package com.example.bookly.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookly.model.UserBook
import com.example.bookly.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class UserBookViewModel @Inject constructor(var userRepository: UserRepository): ViewModel() {

    //private val userRepository = UserRepository()
    //var userBookList = MutableLiveData<List<UserBook>>()

    companion object {
        var userBookList = MutableLiveData<List<UserBook>>()
    }

    init {
        loadAllUserBooks()
        userBookList = userRepository.getUserBookList()
    }

    fun loadAllUserBooks() {
        userRepository.getAllUserBooks()
    }

    fun addBookToUserList(userBook: UserBook) {
        userRepository.addUserBook(userBook)
    }

    suspend fun checkIfBookExistsInUserList(bookId: String): Boolean {
        return userRepository.checkIfBookExistsInUserList(bookId)
    }

    fun deleteUserBook(bookId: String) {
        userRepository.deleteUserBook(bookId)
    }

    fun updateUserBook(bookId: String, readPage: Int) {
        userRepository.updateUserBook(bookId, readPage)
    }

}