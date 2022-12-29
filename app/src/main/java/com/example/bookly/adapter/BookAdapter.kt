package com.example.bookly.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bookly.BookListener
import com.example.bookly.R
import com.example.bookly.databinding.CardBookItemBinding
import com.example.bookly.model.Book
import com.squareup.picasso.Picasso

class BookAdapter(private val mContext: Context, private val bookList: List<Book>, private val bookListener: BookListener)
    : RecyclerView.Adapter<BookAdapter.CardViewItemsHolder>() {

    inner class CardViewItemsHolder(binding: CardBookItemBinding):  RecyclerView.ViewHolder(binding.root) {
        var binding:CardBookItemBinding
        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewItemsHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        val binding:CardBookItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.card_book_item,parent, false)
        return CardViewItemsHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewItemsHolder, position: Int) {
        val book = bookList[position]
        Picasso.get().load(book.imageUrl).into(holder.binding.imageViewBookImage)
        holder.binding.textViewBookName.text = book.bookName
        holder.binding.ratingBarBookRating.rating = book.bookRating

        holder.binding.cardBook.setOnClickListener {
            bookListener.onBookClicked(book)
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}