package com.hirin.story.ui.localization

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.hirin.story.data.localization.Localization
import com.hirin.story.databinding.FragmentLocalizationBottomSheetBinding
import com.jtracker.ui.base.BaseBottomSheet
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocalizationBottomSheet(
    private val listener: LocalizationListener
) : BaseBottomSheet<FragmentLocalizationBottomSheetBinding>() {
    // <editor-fold defaultstate="collapsed" desc="initialize data">
    private val viewModel: LocalizationViewModel by viewModel()
    private val localizationAdapter by lazy {
        LocalizationAdapter(emptyList(), ::onItemClicked)
    }
    // </editor-fold>

    override fun getViewBinding(): FragmentLocalizationBottomSheetBinding =
        FragmentLocalizationBottomSheetBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        this.binding.viewModel = viewModel.also {
            it.localizationLiveData.observe(viewLifecycleOwner, ::handleLocalization)

            viewModel.setDataLocalizationSearch("")
        }
    }

    private fun initUI() {
        binding.fbSearch.doOnTextChanged { text, start, before, count ->
            viewModel.setDataLocalizationSearch(text.toString())
        }

        binding.btClose.setOnClickListener {
            listener.localizationSelected(null)
        }

        binding.rvLocalize.adapter = localizationAdapter
    }

    private fun handleLocalization(data: List<Localization>) {
        localizationAdapter.items = data
    }

    private fun onItemClicked(data: Localization) {
        listener.localizationSelected(data)
    }

    interface LocalizationListener {
        fun localizationSelected(data: Localization?)
    }
}