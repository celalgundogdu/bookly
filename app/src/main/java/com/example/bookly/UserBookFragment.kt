package com.example.bookly

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookly.adapter.UserBookAdapter
import com.example.bookly.databinding.FragmentUserBookBinding
import com.example.bookly.model.UserBook
import com.example.bookly.viewmodel.UserBookViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserBookFragment : Fragment() {

    private lateinit var binding: FragmentUserBookBinding
    private lateinit var userBookList: ArrayList<UserBook>
    private lateinit var viewModel: UserBookViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_book, container, false)
        binding.userBookFragment = this

        userBookList = ArrayList<UserBook>()

        binding.rvUserbooks.setHasFixedSize(true)
        binding.rvUserbooks.layoutManager = LinearLayoutManager(activity)

        binding.lifecycleOwner = this

        UserBookViewModel.userBookList.observe(viewLifecycleOwner){
            val adapter = UserBookAdapter(requireContext(), it, viewModel)
            binding.userBookAdapter = adapter
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: UserBookViewModel by viewModels()
        this.viewModel = tempViewModel
    }
}