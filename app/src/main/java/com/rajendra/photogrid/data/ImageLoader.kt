package com.rajendra.photogrid.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.LinkedHashMap

class ImageLoader {

    // Memory cache
    private val memoryCache = LinkedHashMap<String, Bitmap>(100, 0.75f, true)

    suspend fun loadImage(url: String): Bitmap? {
        // Check memory cache
        memoryCache[url]?.let {
            return it
        }

        // Fetch image from the URL
        val bitmap = fetchImage(url)

        // Add to memory cache
        bitmap?.let {
            memoryCache[url] = it
        }
        return bitmap
    }

    private suspend fun fetchImage(url: String): Bitmap? = withContext(Dispatchers.IO) {
        val connection = URL(url).openConnection() as HttpURLConnection
        try {
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            return@withContext BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            return@withContext null
        } finally {
            connection.disconnect()
        }
    }
}
