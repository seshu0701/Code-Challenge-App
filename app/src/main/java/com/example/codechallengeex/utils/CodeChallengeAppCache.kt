package com.example.codechallengeex.utils

import android.content.Context
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 * Created by Satya Seshu on 10/04/22.
 */
object CodeChallengeAppCache {

    private val CACHE_LOCATION_DISK = 1
    private val CACHE_PREFERENCE = "application_cache"

    fun putData(key: String, pContext: Context, dataToCache: Any?) {
        saveDataToDisk(key, pContext, dataToCache)
    }

    fun getData(key: String, pContext: Context): Any? {
        var lReturnData: Any? = null
        val prefs = pContext.getSharedPreferences(
            pContext.packageName + "_preferences",
            Context.MODE_PRIVATE
        )
        val data = prefs.getString(key, null)
        if (data != null) {
            try {
                lReturnData = deserializeObjectFromString(data)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return lReturnData
    }

    private fun saveDataToDisk(key: String, pContext: Context, dataToCache: Any?) {
        val prefs = pContext.getSharedPreferences(
            pContext.packageName + "_preferences",
            Context.MODE_PRIVATE
        )
        val lEditor = prefs.edit()
        try {
            lEditor.putString(key, serializeObjectToString(dataToCache))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        lEditor.apply()
    }

    @Throws(Exception::class)
    private fun serializeObjectToString(data: Any?): String {

        val byteArrayOutputStream = ByteArrayOutputStream()
        val gzipOut = GZIPOutputStream(byteArrayOutputStream)
        val objectOut = ObjectOutputStream(gzipOut)
        objectOut.writeObject(data)
        objectOut.close()
        val bytes = byteArrayOutputStream.toByteArray()
        return String(Base64.encode(bytes, Base64.DEFAULT))
    }

    @Throws(Exception::class)
    private fun deserializeObjectFromString(objectString: String): Any {
        val bytes = Base64.decode(objectString, Base64.DEFAULT)
        val byteArrayInputStream = ByteArrayInputStream(bytes)
        val gzipIn = GZIPInputStream(byteArrayInputStream)
        val objectIn = ObjectInputStream(gzipIn)
        val lObject = objectIn.readObject()
        objectIn.close()

        return lObject
    }
}