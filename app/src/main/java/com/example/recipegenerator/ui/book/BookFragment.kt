package com.example.recipegenerator.ui.book

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipegenerator.R
import com.example.recipegenerator.adapters.Home2FragmentAdapter
import com.example.recipegenerator.adapters.OnItemClickListener
import com.example.recipegenerator.database.AppDatabase
import com.example.recipegenerator.ui.home.Home3Fragment
import org.w3c.dom.Text

class BookFragment : Fragment(), OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Home2FragmentAdapter
    private lateinit var emptyTextView : TextView
    var dish = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_book, container, false)
        val db = AppDatabase.getDb(requireActivity())
        var dishRecipe = mutableListOf<String>()

        Thread {
            dish = db.getDao().getDish()
            // Обновление интерфейса должно происходить на основном потоке
            requireActivity().runOnUiThread {
                recyclerView = view.findViewById(R.id.rcView)
                emptyTextView = view.findViewById(R.id.emptyTextView)
                recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
                adapter = Home2FragmentAdapter(dish, this)
                recyclerView.adapter = adapter

                adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                    override fun onChanged() {
                        super.onChanged()
                        checkEmptyState()
                    }

                    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                        super.onItemRangeInserted(positionStart, itemCount)
                        checkEmptyState()
                    }

                    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                        super.onItemRangeRemoved(positionStart, itemCount)
                        checkEmptyState()
                    }
                })
                checkEmptyState()
            }
        }.start()

        return view
    }

    override fun onItemClick(position: Int) {
        val db = AppDatabase.getDb(requireActivity())
        var dishRecipe = mutableListOf<String>()
        Thread {
            dishRecipe = db.getDao().getRecipe()
            requireActivity().runOnUiThread {
                val supportFragmentManager = requireActivity().supportFragmentManager
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, Home3Fragment(dishRecipe[position], dish[position], true))
            .addToBackStack(null)
                    .commit()
            }
        }.start()

    }
    private fun checkEmptyState() {
        if (adapter.itemCount == 0) {
            emptyTextView.visibility = View.VISIBLE
        } else {
            emptyTextView.visibility = View.GONE
        }
    }
}