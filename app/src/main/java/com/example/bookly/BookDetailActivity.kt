package com.example.bookly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.bookly.adapter.UserBookAdapter
import com.example.bookly.databinding.ActivityBookDetailBinding
import com.example.bookly.model.Book
import com.example.bookly.model.UserBook
import com.example.bookly.viewmodel.HomeViewModel
import com.example.bookly.viewmodel.UserBookViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class BookDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailBinding
    private val viewModel: UserBookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_detail)

        val book = intent.getSerializableExtra("book") as? Book
        Picasso.get().load(book?.imageUrl).into(binding.imageViewBook)
        binding.textViewName.text = book?.bookName
        binding.textViewDescription.text = book?.description
        binding.ratingBar.rating = book?.bookRating!!

        binding.lifecycleOwner = this

        var exists: Boolean?
        binding.buttonAdd.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                exists = viewModel.checkIfBookExistsInUserList(book.id)
                withContext(Dispatchers.Main) {
                    if(exists == false) {
                        val userBook = UserBook(book.id, book.imageUrl, book.page,0)
                        viewModel.addBookToUserList(userBook)
                    } else {
                        Toast.makeText(this@BookDetailActivity, "This book already exists in your list", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}