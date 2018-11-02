package net.cat_ears.kanmusuviewer.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.cat_ears.kanmusuviewer.model.ShipImageDownloader.download
import java.io.File

class ShipImageLoader(private val cacheRepository: IShipImageCacheRepository) {
    private var counter:Int = 0
    val dlCurrent: MutableLiveData<Int> = MutableLiveData()
    val dlMax: MutableLiveData<Int> = MutableLiveData()
    private var isCallCancel: Boolean = false
    val isDownloading: MutableLiveData<Boolean> = MutableLiveData()

    fun loadImage(ship: Ship?, isDamaged: Boolean): Bitmap? {
        if (ship == null) return null
        if (1500 < ship.id && isDamaged) return null

        val cache = cacheRepository.cacheFile(ship, isDamaged)
        if (cache.exists()) {
            val bytes = cache.readBytes()
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        val bytes = ship.download(isDamaged) ?: return null
        cache.writeBytes(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun downloadAllImages(ships: List<Ship>?) {
        if (isDownloading.value == true)
            return

        if(ships == null){
            isDownloading.postValue(false)
            isCallCancel = false
            return
        }

        dlMax.postValue(ships.size)
        isDownloading.postValue(true)

        GlobalScope.launch {
            for (ship in ships) {
                if (isCallCancel)
                    break
                dlCurrent.postValue(++counter)
                val undamagedJob = launch { ship.fillCache(false) }
                if (ship.id <= 1500) {
                    ship.fillCache(true)
                }
                undamagedJob.join()
            }
        }.invokeOnCompletion {
            counter = 0
            isCallCancel = false
            isDownloading.postValue(false)
            dlCurrent.postValue(0)
        }
    }

    fun cancelDownload() {
        isCallCancel = true
    }

    private fun Ship.fillCache(isDamaged: Boolean) {
        val cache = cacheRepository.cacheFile(this, isDamaged)
        if (cache.exists()) return
        val bytes = this.download(isDamaged) ?: return
        cache.writeBytes(bytes)
    }

}