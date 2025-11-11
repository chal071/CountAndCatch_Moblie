package com.example.countandcatch.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object JsonHelper {

    val gson = Gson()
    val FILE_NAME = "countAndCatch.json"

    inline fun <reified T> loadList(context: Context): List<T> {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) return emptyList()
        val json = file.readText()
        val type = object : TypeToken<List<T>>() {}.type
        return gson.fromJson(json, type)
    }

    fun <T> saveList(context: Context, data: List<T>) {
        val json = gson.toJson(data)
        val file = File(context.filesDir, FILE_NAME)
        file.writeText(json)
    }

    fun clear(context: Context) {
        val file = File(context.filesDir, FILE_NAME)
        if (file.exists()) file.delete()
    }

}
