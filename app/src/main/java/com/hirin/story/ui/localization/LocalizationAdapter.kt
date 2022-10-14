package com.hirin.story.ui.localization

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hirin.story.data.localization.Localization

class LocalizationAdapter (
    _items: List<Localization>,
    private val onItemClick: (Localization) -> Unit
): RecyclerView.Adapter<LocalizationViewHolder>() {
    var items = _items
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalizationViewHolder {
        return LocalizationViewHolder.create(parent, onItemClick)
    }

    override fun onBindViewHolder(holder: LocalizationViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

}