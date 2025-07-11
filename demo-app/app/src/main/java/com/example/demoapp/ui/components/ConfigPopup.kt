package com.example.demoapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConfigPopup(
    setShowDialog: (Boolean) -> Unit,
    updateConfig: (String, String) -> Unit
) {
    var moduleName by remember { mutableStateOf("") }
    var fieldNames by remember { mutableStateOf(listOf("")) }

    AlertDialog(
        onDismissRequest = { setShowDialog(false) },
        confirmButton = {},
        dismissButton = {},
        title = { Text("Configuration") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Edit Config", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))

                TextField(
                    value = moduleName,
                    onValueChange = { moduleName = it },
                    label = { Text("Module Name") },
                    placeholder = { Text("What type of input text? e.g. Incident Report") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))
                Text("Field Names:")

                // Render dynamic field name inputs
                fieldNames.forEachIndexed { index, value ->
                    TextField(
                        value = value,
                        onValueChange = { string ->
                            fieldNames = fieldNames.toMutableList().also { it[index] = string }
                        },
                        label = { Text("Field ${index + 1}") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }

                // Add field button
                Button(
                    onClick = {
                        fieldNames = fieldNames + ""
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("+ Add Field")
                }

                Spacer(Modifier.height(16.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(onClick = { setShowDialog(false) }) {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        val fields = fieldNames.joinToString(", ")
                        updateConfig(moduleName, fields)
                        setShowDialog(false)
                    }) {
                        Text("Save")
                    }
                }
            }
        }
    )
}