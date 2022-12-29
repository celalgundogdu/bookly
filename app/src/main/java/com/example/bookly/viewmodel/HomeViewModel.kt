package com.example.bookly.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookly.model.Book
import com.example.bookly.repo.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(var bookRepository: BookRepository): ViewModel() {

    //private val bookRepository = BookRepository()
    var bookList = MutableLiveData<List<Book>>()

    init{
        loadAllBooks()
        bookList = bookRepository.getBookList()
    }

    fun loadAllBooks() {
        bookRepository.getAllBooks()
    }

    fun searchBook(query: String) {
        bookRepository.searchBook(query)
    }
}