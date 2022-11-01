package com.hirin.story.ui.main.pages.momentlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.hirin.story.databinding.LoadingItemBinding

class LoadingStateViewHolder(
    val binding: LoadingItemBinding,
    private val onItemClick: () -> Unit,
): RecyclerView.ViewHolder(binding.root) {
    fun bind(loadState: LoadState) {
        binding.apply {
            if (loadState is  LoadState.Error)
                binding.tbError.text = loadState.error.localizedMessage
            binding.pbLoading.isVisible = loadState is LoadState.Loading
            binding.btRetry.isVisible = loadState is LoadState.Error
            binding.tbError.isVisible = loadState is LoadState.Error
            btRetry.setOnClickListener { onItemClick() }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClicked: () -> Unit
        ): LoadingStateViewHolder {
            val view = LoadingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return LoadingStateViewHolder(view, onItemClicked)
        }
    }
}