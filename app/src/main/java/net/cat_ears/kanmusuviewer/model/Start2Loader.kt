package net.cat_ears.kanmusuviewer.model

import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File

class Start2Loader(private val filesDir: File) {

    fun loadFromLocal(): List<Ship> {
        val file = File(filesDir, FILENAME_START2)
        if (!file.exists()) return listOf()

        val text = file.readText()
        return parse(text) ?: listOf()
    }

    fun parseAndSave(text: String): List<Ship>? {
        val ships = parse(text)
        if (ships != null) {
            save(text)
        }
        return ships
    }

    private fun parse(text: String): List<Ship>? {
        val json = text.removePrefix("svdata=")
        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        try {
            val start2 = moshi.adapter(Start2::class.java).fromJson(json)
            if (start2 == null)
                return null

            return start2.apiData.apiMstShip
                    .map{ s -> Ship.parse(s, start2.apiData.apiMstShipgraph.find{ g ->  s.apiId == g.apiId})}
        } catch (e: JsonEncodingException) {
            return null
        }
    }

    private fun save(text: String) = File(filesDir, FILENAME_START2).writeText(text)

    companion object {
        private val FILENAME_START2: String = "start2"
    }
}