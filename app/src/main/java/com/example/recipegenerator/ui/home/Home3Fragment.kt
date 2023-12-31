package com.example.recipegenerator.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.recipegenerator.R
import com.example.recipegenerator.database.AppDatabase
import com.example.recipegenerator.database.Recipe
class Home3Fragment(private val text: String, private val dish: String, private var isRecipeSaved : Boolean = false) : Fragment() {
    private lateinit var saveButton: ImageButton
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home3, container, false)
        val recipeText: TextView = view.findViewById(R.id.recipeText)
        val backButton: Button = view.findViewById(R.id.backButton)
        saveButton = view.findViewById(R.id.saveButton)
        db = AppDatabase.getDb(requireContext())
        recipeText.text = text
        updateButtonUI()

        saveButton.setOnClickListener {

            if (!isRecipeSaved) {
                val item = Recipe(null, dish, text)
                Thread {
                    db.getDao().insertItem(item)
                    isRecipeSaved = true
                    requireActivity().runOnUiThread {
                        updateButtonUI()
                    }
                }.start()
            } else {
                Thread {
                    db.getDao().deleteRecipeByDishName(dish)
                    isRecipeSaved = false
                    requireActivity().runOnUiThread {
                        updateButtonUI()
                    }
                }.start()
            }
        }

        backButton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            if (fragmentManager.backStackEntryCount > 0) {
                fragmentManager.popBackStack()
            } else {
                // Handle your back button functionality
            }
        }
        return view
    }

    private fun updateButtonUI() {
        if (isRecipeSaved) {
            saveButton.setImageResource(R.drawable.saved)
        } else {
            saveButton.setImageResource(R.drawable.save)
        }
    }
}
