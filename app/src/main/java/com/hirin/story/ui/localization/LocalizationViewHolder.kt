package com.hirin.story.ui.localization

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.hirin.story.R
import com.hirin.story.data.localization.Localization
import com.hirin.story.databinding.LocalizationItemBinding

class LocalizationViewHolder (
    val binding: LocalizationItemBinding,
    private val onItemClick: (Localization) -> Unit,
    private val parent: ViewGroup
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Localization) {
        binding.apply {
            igCountry.setImageDrawable(ContextCompat.getDrawable(parent.context, item.drawable))
            tbName.text = item.name
            cvLocalize.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClicked: (Localization) -> Unit
        ): LocalizationViewHolder {
            val view = DataBindingUtil
                .inflate<LocalizationItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.localization_item,
                    parent, false
                )
            return LocalizationViewHolder(view, onItemClicked, parent)
        }
    }
}