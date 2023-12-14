package com.example.recipegenerator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.recipegenerator.R

class Home2FragmentAdapter(private val items: MutableList<String>, private val listener: OnItemClickListener) : RecyclerView.Adapter<Home2FragmentAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Home2FragmentAdapter.ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ItemViewHolder(view)
    }
    override fun onBindViewHolder(holder: Home2FragmentAdapter.ItemViewHolder, position: Int) {
        val itemName = items[position]
        holder.bind(itemName)
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    override fun getItemCount() = items.size

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemNameTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
        fun bind(itemName: String) {
            itemNameTextView.text = itemName
        }
    }
    fun updateData(newItems: List<String>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}