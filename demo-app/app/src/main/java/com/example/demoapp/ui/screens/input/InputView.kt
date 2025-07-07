package com.example.demoapp.ui.screens.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.demoapp.ui.Screen

@Composable
fun InputScreen(
    viewModel: InputViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val uiState: InputState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .heightIn(max = 500.dp)
                .fillMaxWidth(fraction = 0.9f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = uiState.inputText,
                label = { Text("Enter text") },
                onValueChange = { viewModel.setInputText(it) },
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .fillMaxWidth(0.95f)
            )
            Button(
                onClick = {
                    viewModel.generateSummary()
                    navController.navigate(Screen.Output.name)
                },
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .requiredWidth(width = 200.dp)
            ) {
                Text("Generate Summary")
            }
        }
    }
}