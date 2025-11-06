package com.example.countandcatch

import androidx.lifecycle.ViewModel
import com.example.countcatch.CountGameLevelConfig

class CountMatchViewModel : ViewModel() {
    var level: CountGameLevelConfig? = null
    val userLinks = mutableMapOf<Int, String>()
    var startTimeMs: Long = 0
}
