package net.cat_ears.kanmusuviewer.model

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException

class Model(filesDir: File?, cacheDir: File?) {
    val ships: MutableLiveData<List<Ship>> = MutableLiveData()
    val shipImageLoader: ShipImageLoader
    val errorOnLoadingStart2: MutableLiveData<Unit> = MutableLiveData()
    val isLoading: MediatorLiveData<Boolean> = MediatorLiveData()
    private val start2Loader: Start2Loader

    init {
        if (filesDir == null)
            throw FileNotFoundException("ExternalFilesDir Not Found.")
        if (cacheDir == null)
            throw FileNotFoundException("ExternalCacheDir Not Found.")
        start2Loader = Start2Loader(filesDir)
        shipImageLoader = ShipImageLoader(cacheDir)

        isLoading.addSource(shipImageLoader.isDownloading) {
            isLoading.postValue(shipImageLoader.isDownloading.value)
        }
    }

    fun loadStart2FromLocal() {
        if (isLoading.value == true)
            return
        GlobalScope.launch {
            isLoading.postValue(true)
            ships.postValue(start2Loader.loadFromLocal())
        }.invokeOnCompletion { isLoading.postValue(false) }
    }

    fun parseAndSaveStart2(text: String) {
        if (isLoading.value == true)
            return
        GlobalScope.launch {
            isLoading.postValue(true)
            val previousList = ships.value
            // 一端空にして読み込み中に操作できなくする
            ships.postValue(listOf())
            val newShips = start2Loader.parseAndSave(text)
            if (newShips != null) {
                ships.postValue(newShips)
            } else {
                errorOnLoadingStart2.postValue(Unit)
                ships.postValue(previousList)
            }
        }.invokeOnCompletion {
            isLoading.postValue(false)
        }
    }

    fun downloadAllImages() {
        if (ships.value.isNullOrEmpty())
            return
        if (isLoading.value == true)
            return

        shipImageLoader.downloadAllImages(ships.value!!)
    }

    fun cancelDownload() {
        shipImageLoader.cancelDownload()
    }
}