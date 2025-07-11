package com.example.demoapp.ui.screens.input

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.example.demoapp.MainApplication
import com.example.demoapp.data.MainRepository
import com.example.demoapp.model.ModelManager
import com.example.demoapp.ui.screens.output.OutputState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class InputState (
    val inputText: String = ""
)

class InputViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    // Private mutable state
    private val _uiState: MutableStateFlow<InputState> = MutableStateFlow<InputState>(
        InputState()
    )

    // Public immutable state exposed
    val uiState: StateFlow<InputState> = _uiState.asStateFlow()

    // State setter
    fun setInputText(text: String) {
        _uiState.value = InputState(text)
    }

    fun generateSummary() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("InputVM", "Generating inference for: ${uiState.value.inputText}")
            mainRepository.generateSummary(uiState.value.inputText)
            setInputText("")
        }
    }

    fun updateModule(moduleName: String, fieldNamesLiteral: String) {
        mainRepository.updateModule(moduleName, fieldNamesLiteral)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MainApplication)
                val mainRepository = application.container.mainRepository
                InputViewModel(mainRepository)
            }
        }
    }
}