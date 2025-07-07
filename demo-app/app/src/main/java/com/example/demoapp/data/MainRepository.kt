package com.example.demoapp.data

import android.graphics.ColorSpace
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.demoapp.model.ModelManager
import com.example.demoapp.model.Fields
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface MainRepositoryInterface {
    fun generateSummary(input: String)
}

sealed interface ModelState{
    data class Completion(val fields: Fields) : ModelState
    object Loading : ModelState
    object Empty : ModelState
}

class MainRepository(
    private val modelManager: ModelManager
) : MainRepositoryInterface {

    // Private mutable state
    private val _modelState: MutableStateFlow<ModelState> = MutableStateFlow<ModelState>(
        ModelState.Empty
    )

    // Public immutable state exposed
    val modelState: StateFlow<ModelState> = _modelState.asStateFlow()

    fun setState(newState: ModelState) {
        _modelState.value = newState
    }

    override fun generateSummary(input: String) {
        setState(ModelState.Loading)
        val summary: Fields = modelManager.summarize(input)
        setState(ModelState.Completion(summary))
    }
}