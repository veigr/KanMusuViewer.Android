package net.cat_ears.kanmusuviewer.model

import java.io.File

interface IShipImageCacheRepository {
    fun cacheFile(ship: Ship, isDamaged: Boolean): IShipImageCache
}

interface IShipImageCache {
    fun exists(): Boolean
    fun writeBytes(bytes: ByteArray)
    fun readBytes(): ByteArray
}

class ShipImageCacheRepository(private val cacheDir: File) : IShipImageCacheRepository {
    override fun cacheFile(ship: Ship, isDamaged: Boolean): IShipImageCache {
        val path = "${ship.id.toString().padStart(4, '0')}_${ship.imageVersion.toString().padStart(3, '0')}_${if (isDamaged) "damaged" else "undamaged"}.png"
        return ShipImageCache(File(cacheDir, path))
    }
}

class ShipImageCache(private val cacheFile: File) : IShipImageCache {
    override fun exists(): Boolean = cacheFile.exists()

    override fun writeBytes(bytes: ByteArray) {
        if(!cacheFile.parentFile.exists()) cacheFile.parentFile.mkdir()
        cacheFile.writeBytes(bytes)
    }

    override fun readBytes(): ByteArray = cacheFile.readBytes()
}