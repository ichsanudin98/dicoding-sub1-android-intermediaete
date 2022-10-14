package com.hirin.story.ui.localization

import com.hirin.story.R
import com.hirin.story.data.localization.Localization
import com.hirin.story.ui.base.BaseViewModel
import com.hirin.story.utils.PostLiveData

class LocalizationViewModel: BaseViewModel() {
    internal val localizationLiveData = PostLiveData<List<Localization>>()
    val allData = mutableListOf<Localization>()
    val allDataSearch = mutableListOf<Localization>()

    init {
        allData.clear()
        allDataSearch.clear()
        val id = Localization("in", "Indonesia", R.drawable.ig_flag_indonesian, false)
        val usa = Localization("en", "United States of America", R.drawable.ig_flag_usa, false)
        allData.add(id)
        allData.add(usa)
        allDataSearch.addAll(allData)
    }

    fun setDataLocalizationSearch(text: String) {
        allDataSearch.clear()
        if (text.isEmpty()) {
            allDataSearch.addAll(allData)
        } else {
            allData.forEach {
                if (it.name.contains(text, true))
                    allDataSearch.add(it)
            }
        }
        localizationLiveData.postValue(allDataSearch)
    }
}