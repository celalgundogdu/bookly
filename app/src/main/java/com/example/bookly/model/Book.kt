package com.example.bookly.model

data class Book(var id: String,
                var bookName:String,
                var bookRating: Float,
                var imageUrl: String,
                var description: String,
                var page: Int) : java.io.Serializable{
}