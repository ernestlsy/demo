package com.example.demoapp.data

import android.content.Context
import com.example.modularsummarizer.ModularSummarizer
import com.example.modularsummarizer.ModularSummarizerInstance
import com.example.demoapp.model.ModelManager
import com.example.demoapp.network.ApiService
import java.io.File
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val modelManager: ModelManager
    val mainRepository: MainRepository
}

class DefaultAppContainer(
    val context: Context
): AppContainer {

    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.8.224:5000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api = retrofit.create(ApiService::class.java)

    val model = File(context.getExternalFilesDir(null), "model.task")
    val modelPath = model.absolutePath

    val moduleName = "Incident Report"
//    val fieldNames = "title, incident_type, date_time, location, cause, issue, resolution"
    val fieldNames = "date_time, location, issue, resolution"

    val modularInference: ModularSummarizer by lazy {
        ModularSummarizerInstance(moduleName, fieldNames, context, modelPath, maxTopK = 40, maxTokens = 256)
    }

    override val modelManager: ModelManager by lazy {
        ModelManager(modularInference)
    }

    override val mainRepository: MainRepository by lazy {
        MainRepository(modelManager, api)
    }

}