package com.example.demoapp.data

import android.content.Context
import com.example.modularsummarizer.ModularSummarizer
import com.example.modularsummarizer.ModularSummarizerInstance

import com.example.demoapp.model.ModelManager
import java.io.File

interface AppContainer {
    val modelManager: ModelManager
    val mainRepository: MainRepository
}

class DefaultAppContainer(
    val context: Context
): AppContainer {

    val model = File(context.getExternalFilesDir(null), "model.task")
    val modelPath = model.absolutePath

    val moduleName = "Incident Report"
    val fieldNames = "title, incident_type, date_time, location, cause, issue, resolution"

    val modularInference: ModularSummarizer by lazy {
        ModularSummarizerInstance(moduleName, fieldNames, context, modelPath, maxTopK = 10, maxTokens = 256)
    }

    override val modelManager: ModelManager by lazy {
        ModelManager(modularInference)
    }

    override val mainRepository: MainRepository by lazy {
        MainRepository(modelManager)
    }

}