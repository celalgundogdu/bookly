package com.example.bookly

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookly.adapter.BookAdapter
import com.example.bookly.databinding.FragmentHomeBinding
import com.example.bookly.model.Book
import com.example.bookly.viewmodel.HomeViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), BookListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bookList: ArrayList<Book>
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.homeFragment = this

        bookList = ArrayList<Book>()

        binding.rvBooks.setHasFixedSize(true)
        binding.rvBooks.layoutManager = LinearLayoutManager(activity)
        //binding.rvBooks.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)

        viewModel.bookList.observe(viewLifecycleOwner) {
            val adapter = BookAdapter(requireContext(), it, this)
            binding.bookAdapter = adapter
        }

        searchBook()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: HomeViewModel by viewModels()
        this.viewModel = tempViewModel
    }

    fun searchBook() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchBook(query)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                getAllBooks()
                return false
            }
        })
    }

    fun getAllBooks() {
        return viewModel.loadAllBooks()
    }

    override fun onBookClicked(book: Book) {
        startActivity(Intent(activity, BookDetailActivity::class.java).putExtra("book", book))
    }
}