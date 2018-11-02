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
        return Start2.parse(text) ?: listOf()
    }

    fun parseAndSave(text: String): List<Ship>? {
        val ships = Start2.parse(text)
        if (ships != null) {
            save(text)
        }
        return ships
    }

    private fun save(text: String) = File(filesDir, FILENAME_START2).writeText(text)

    companion object {
        private val FILENAME_START2: String = "start2"
    }
}