package com.example.demoapp.ui.screens.output

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.demoapp.ui.components.FieldBox

@Composable
fun OutputScreen(
    viewModel: OutputViewModel,
    navController: NavController,
    modifier: Modifier
) {
    val uiState: OutputState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(fraction = 0.90f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState is OutputState.Empty) {
            Text("No summary generated")
        } else if (uiState is OutputState.Loading) {
            Text("Generating summary...")
        } else {
            val fields = (uiState as OutputState.Editing).fields
            fields.values.forEachIndexed { index, value ->
                FieldBox(
                    name = fields.fieldNames[index],
                    value = value,
                    setValue = { value -> viewModel.setState(fields.editValue(index, value)) },
                    clearValue = {
                        viewModel.setState(fields.editValue(index, ""))
                        Log.d("UI", "Clearing for index $index")
                    }
                )
                Spacer(modifier = modifier.height(16.dp))
            }
            Button(
                onClick = { viewModel.submitFields() }
            ) {
                Text("Submit")
            }
        }
    }
}