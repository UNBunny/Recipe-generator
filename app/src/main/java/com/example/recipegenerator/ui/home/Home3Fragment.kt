package com.example.recipegenerator.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.recipegenerator.R

class Home3Fragment (private val text: String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_home3, container, false)
        val recipeText : TextView = view.findViewById(R.id.recipeText)
        val backBuuton : Button = view.findViewById(R.id.backButton)
        recipeText.setText(text)
        backBuuton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            if (fragmentManager.backStackEntryCount > 0) {
                // Возвращаемся к предыдущему фрагменту в стеке
                fragmentManager.popBackStack()
            } else {
            }
        }
        return view
    }

}