package net.cat_ears.kanmusuviewer.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.github.kittinunf.fuel.httpGet
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import kotlin.random.Random

class ShipImageLoader(private val cacheDir:File) {
    val dlCurrent: MutableLiveData<Int> = MutableLiveData()
    val dlMax: MutableLiveData<Int> = MutableLiveData()
    private var isCallCancel: Boolean = false
    val isDownloading: MutableLiveData<Boolean> = MutableLiveData()

    fun loadImage(ship:Ship?, isDamaged: Boolean): Bitmap? {
        if(ship == null) return null
        if (1500 < ship.id && isDamaged) return null

        val localFileName = "${ship.id.toString().padStart(4, '0')}_${ship.imageVersion.toString().padStart(3, '0')}_${if (isDamaged) "damaged" else "undamaged"}.png"
        val file = File(cacheDir, localFileName)
        if (file.exists())
            return BitmapFactory.decodeFile(file.path)

        val url = createUrl(ship, isDamaged)
        Log.d("download", url)

        val (bytes, error) = url
                .httpGet()
                .response()
                .third
        if (error != null) return null
        if (bytes == null) return null
        file.writeBytes(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun downloadAllImages(ships:List<Ship>) {
        if (isDownloading.value == true)
            return

        dlMax.postValue(ships.size)
        isDownloading.postValue(true)

        GlobalScope.launch {
            for (ship in ships) {
                if (isCallCancel)
                    break
                dlCurrent.postValue((dlCurrent.value ?: 0) + 1)
                // 手抜き……
                val undamagedJob = launch { loadImage(ship, false) }
                if (ship.id <= 1500) {
                    loadImage(ship, true)
                }
                undamagedJob.join()
            }
        }.invokeOnCompletion {
            isCallCancel = false
            isDownloading.postValue(false)
            dlCurrent.postValue(0)
        }
    }

    fun cancelDownload() {
        isCallCancel = true
    }

    companion object {

        private fun createUrl(ship: Ship, isDamaged: Boolean): String {
            val path = if (isDamaged) "full_dmg" else "full"
            val suffix = createSuffix(ship.id, path)
            val index = Random.nextInt(0, servers.size)
            val graphSuffix = if(ship.graphFileName != null) "_${ship.graphFileName}" else ""
            return "http://${servers[index]}/kcs2/resources/ship/$path/${ship.id.toString().padStart(4, '0')}_$suffix$graphSuffix.png"
        }

        private fun createSuffix(r: Int, path: String): String {
            val i = "ship_$path"
            val s = createKey(i)
            val a = if (i.isNotEmpty()) i.length else 1
            return (17 * (r + 7) * resource[(s + r * a) % 100] % 8973 + 1e3).toInt().toString()
        }

        private fun createKey(t: String): Int {
            var e = 0
            for (i in 0 until t.length) {
                e += t[i].toInt()
            }
            return e
        }

        private val servers: Array<String> = arrayOf(
                "203.104.209.71",
                "203.104.209.87",
                "125.6.184.215",
                "203.104.209.183",
                "203.104.209.150",
                "203.104.209.134",
                "203.104.209.167",
                "203.104.248.135",
                "125.6.189.7",
                "125.6.189.39",
                "125.6.189.71",
                "125.6.189.103",
                "125.6.189.135",
                "125.6.189.167",
                "125.6.189.215",
                "125.6.189.247",
                "203.104.209.23",
                "203.104.209.39",
                "203.104.209.55",
                "203.104.209.102"
        )

        private val resource: IntArray = intArrayOf(6657, 5699, 3371, 8909, 7719, 6229, 5449, 8561, 2987, 5501, 3127, 9319, 4365, 9811, 9927, 2423, 3439, 1865, 5925, 4409, 5509, 1517, 9695, 9255, 5325, 3691, 5519, 6949, 5607, 9539, 4133, 7795, 5465, 2659, 6381, 6875, 4019, 9195, 5645, 2887, 1213, 1815, 8671, 3015, 3147, 2991, 7977, 7045, 1619, 7909, 4451, 6573, 4545, 8251, 5983, 2849, 7249, 7449, 9477, 5963, 2711, 9019, 7375, 2201, 5631, 4893, 7653, 3719, 8819, 5839, 1853, 9843, 9119, 7023, 5681, 2345, 9873, 6349, 9315, 3795, 9737, 4633, 4173, 7549, 7171, 6147, 4723, 5039, 2723, 7815, 6201, 5999, 5339, 4431, 2911, 4435, 3611, 4423, 9517, 3243)
    }
}