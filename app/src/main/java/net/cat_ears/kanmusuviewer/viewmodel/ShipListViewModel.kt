package net.cat_ears.kanmusuviewer.viewmodel

import androidx.lifecycle.*
import android.view.View
import net.cat_ears.kanmusuviewer.model.Model
import net.cat_ears.kanmusuviewer.model.Ship
import net.cat_ears.kanmusuviewer.model.map
import net.cat_ears.kanmusuviewer.R

class ShipListViewModel(private val model: Model) : ViewModel() {

    val errorMessageId: MediatorLiveData<Int?> = MediatorLiveData()

    val dlCount: LiveData<String> = model.shipImageLoader.dlCurrent.map { "$it / ${model.shipImageLoader.dlMax.value}" }

    val ships: MutableLiveData<List<Ship>> = model.ships

    val isLoading: LiveData<Boolean> = model.isLoading

    val progressVisibility: LiveData<Int> = model.isLoading.map {
        if (it)
            View.VISIBLE
        else
            View.INVISIBLE
    }

    val filterText: MutableLiveData<String> = MutableLiveData()

    init {
        errorMessageId.addSource(model.errorOnLoadingStart2)
        {
            errorMessageId.postValue(R.string.error_load_start2)
        }
    }

    fun loadAndSaveStart2(text: String) = model.parseAndSaveStart2(text)

    fun loadStart2FromLocal() = model.loadStart2FromLocal()

    fun downloadAllImages() = model.downloadAllImages()

    fun cancelDownload() = model.cancelDownload()

    fun filter(text:String?) {
        filterText.postValue(text ?: "")
    }
}