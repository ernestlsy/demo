package com.example.demoapp.model

import android.util.Log
import com.example.modularsummarizer.ModularSummarizer

class ModelManager(
    val modularSummarizer: ModularSummarizer
) {
    fun summarize(input: String): Fields {
        Log.d("Model", "Generating summary for input: $input")
        val (names, values) = modularSummarizer.generateFormattedOutput(
            input = input, logging = true
        )
        return Fields(names, values)
    }
}