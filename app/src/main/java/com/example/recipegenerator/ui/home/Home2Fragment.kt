package com.example.recipegenerator.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipegenerator.GPTProcessor
import com.example.recipegenerator.R
import com.example.recipegenerator.adapters.Home2FragmentAdapter
import com.example.recipegenerator.adapters.OnItemClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Home2Fragment(private val items: MutableList<String>) : Fragment(), OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Home2FragmentAdapter
    private lateinit var backButton: Button
    private lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home2, container, false)
        recyclerView = view.findViewById(R.id.rcView)
        backButton = view.findViewById(R.id.backButton)
        progressBar = view.findViewById(R.id.progressBar)
        backButton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            if (fragmentManager.backStackEntryCount > 0) {
                // Возвращаемся к предыдущему фрагменту в стеке
                fragmentManager.popBackStack()
            } else {
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = Home2FragmentAdapter(items, this) // Initialize the adapter with items
        recyclerView.adapter = adapter
        return view
    }

    override fun onItemClick(position: Int) {
        showProgressBar()
        recyclerView.visibility = View.GONE
        val gptProcessor = GPTProcessor()
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch { // Use Dispatchers.Main for UI-related work
            try {
                val result = // Move network call to IO dispatcher
                    gptProcessor.gptResponse("Помоги составить рецепт для данного блюда: ${items[position]}")
                    val supportFragmentManager = requireActivity().supportFragmentManager
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, Home3Fragment(result, items[position]))
                        .addToBackStack(null)
                        .commit()

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE // Показать ProgressBar
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE // Скрыть ProgressBar
    }
}

