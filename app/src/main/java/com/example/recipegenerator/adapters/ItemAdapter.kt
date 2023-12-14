package com.example.recipegenerator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipegenerator.R
class ItemAdapter(private val items: MutableList<String>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ItemViewHolder(view)
    }
    override fun onBindViewHolder(holder: ItemAdapter.ItemViewHolder, position: Int) {
        val itemName = items[position]
        holder.bind(itemName)
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