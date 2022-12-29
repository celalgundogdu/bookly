package com.example.bookly.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bookly.R
import com.example.bookly.databinding.CardUserbookItemBinding
import com.example.bookly.model.UserBook
import com.example.bookly.viewmodel.UserBookViewModel
import com.squareup.picasso.Picasso

class UserBookAdapter(
    private val mContext: Context,
    private val userBookList: List<UserBook>,
    private val viewModel: UserBookViewModel,
) : RecyclerView.Adapter<UserBookAdapter.CardViewItemsHolder>() {

    inner class CardViewItemsHolder(binding: CardUserbookItemBinding): RecyclerView.ViewHolder(binding.root) {
        var binding: CardUserbookItemBinding
        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewItemsHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        val binding:CardUserbookItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.card_userbook_item, parent, false)
        return CardViewItemsHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewItemsHolder, position: Int) {
        val userBook = userBookList[position]
        Picasso.get().load(userBook.imageUrl).into(holder.binding.imageViewUserbook)
        holder.binding.editTextPageRead.hint = userBook.pageRead.toString() + " / " + userBook.page.toString()

        holder.binding.imageViewUpdate.setOnClickListener{
            if(holder.binding.editTextPageRead.text.toString().isEmpty() ||
                !holder.binding.editTextPageRead.text.toString().matches("-?[0-9]+(\\.[0-9]+)?".toRegex()) ||
                holder.binding.editTextPageRead.text.toString().toInt() < 0 ||
                holder.binding.editTextPageRead.text.toString().toInt() > userBook.page ) {
                Toast.makeText(mContext, "Invalid page number", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.updateUserBook(userBook.bookId, holder.binding.editTextPageRead.text.toString().toInt())
            }
        }

        holder.binding.imageViewDelete.setOnClickListener{
            viewModel.deleteUserBook(userBook.bookId)
        }
    }

    override fun getItemCount(): Int {
        return userBookList.size
    }
}