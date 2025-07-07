package com.example.demoapp

import android.app.Application
import com.example.demoapp.data.AppContainer
import com.example.demoapp.data.DefaultAppContainer

class MainApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(applicationContext)
    }
}