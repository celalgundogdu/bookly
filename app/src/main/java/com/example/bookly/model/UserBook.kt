package com.example.bookly.model

data class UserBook(var bookId: String,
                    var imageUrl: String,
                    var page: Int,
                    var pageRead: Int) {
}