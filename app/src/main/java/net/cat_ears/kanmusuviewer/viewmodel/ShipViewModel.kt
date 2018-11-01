package net.cat_ears.kanmusuviewer.viewmodel

import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import net.cat_ears.kanmusuviewer.R
import net.cat_ears.kanmusuviewer.model.Model
import net.cat_ears.kanmusuviewer.model.Ship

class ShipViewModel(private val model: Model) : ViewModel() {
    val id: MutableLiveData<Int> = MutableLiveData()
    val name: MutableLiveData<String> = MutableLiveData()
    val image: MutableLiveData<Bitmap?> = MutableLiveData()
    val imageDamaged: MutableLiveData<Bitmap?> = MutableLiveData()
    val progressVisibilityUndamaged: MutableLiveData<Int> = MutableLiveData()
    val progressVisibilityDamaged: MutableLiveData<Int> = MutableLiveData()
    val errorMessageId: MediatorLiveData<Int?> = MediatorLiveData()
    private var selectedShip: Ship? = null
    private var currentJob: Job = GlobalScope.launch { }

    fun selectShip(nextId: Int) {
        clear()

        selectedShip = model.ships.value?.first { it.id == nextId }
        if (selectedShip == null) return

        id.postValue(selectedShip?.id)
        name.postValue(selectedShip?.name)

        if (!currentJob.isCompleted) {
            currentJob.cancel()
        }
        currentJob = GlobalScope.launch {
            launch(this.coroutineContext) {
                loadImages(this, false)
            }
            if (nextId <= 1500) {
                // 敵はダメージ画像がある場合とない場合があるが、あっても同じなので不要
                loadImages(this, true)
            }
        }
    }

    private fun loadImages(coroutineScope: CoroutineScope, isDamaged: Boolean) {
        val targetVisibility: MutableLiveData<Int> = if (isDamaged)
            progressVisibilityDamaged
        else progressVisibilityUndamaged

        val targetImage: MutableLiveData<Bitmap?> = if (isDamaged)
            imageDamaged
        else image

        targetVisibility.postValue(View.VISIBLE)
        val newImage = model.shipImageLoader.loadImage(selectedShip, isDamaged)

        if (!coroutineScope.isActive)
            return

        if (newImage != null) {
            targetImage.postValue(newImage)
        } else {
            errorMessageId.postValue(R.string.error_load_image)
        }
        targetVisibility.postValue(View.INVISIBLE)
    }

    private fun clear() {
        id.postValue(null)
        name.postValue(null)
        image.postValue(null)
        imageDamaged.postValue(null)
    }
}