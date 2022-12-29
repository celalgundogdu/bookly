package com.example.bookly

import com.example.bookly.model.Book

interface BookListener {
    fun onBookClicked(book: Book)
}
