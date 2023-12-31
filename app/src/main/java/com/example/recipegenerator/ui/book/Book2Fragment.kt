package com.example.recipegenerator.ui.book

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.recipegenerator.R

class Book2Fragment (val dishRecipe : String) : Fragment() {
    private lateinit var tvText: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_book2, container, false)
        tvText = view.findViewById(R.id.tvText)
        tvText.text = dishRecipe
        return view
    }

}