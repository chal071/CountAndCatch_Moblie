package com.example.countandcatch

import android.content.Context
import com.example.countcatch.CountGameLevelConfig
import com.google.gson.Gson

fun Context.loadLevelConfig(levelId: Int): CountGameLevelConfig {
    val path = "levels/level_${levelId}.json"
    val json = assets.open(path).bufferedReader().use { it.readText() }
    return Gson().fromJson(json, CountGameLevelConfig::class.java)
}
