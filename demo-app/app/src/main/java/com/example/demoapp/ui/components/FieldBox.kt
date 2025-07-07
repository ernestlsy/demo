package com.example.demoapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FieldBox(
    name: String,
    value: String,
    setValue: (String) -> Unit,
    clearValue: () -> Unit
) {
    TextField(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(0.90f),
        value = value,
        onValueChange = { setValue(it) },
        label = { Text(name) },
        trailingIcon = {
            ClearButton(clearFunction = { clearValue() })
        }
    )
}

@Composable
fun ClearButton(clearFunction: () -> Unit) {
    IconButton(
        onClick = { clearFunction() }
    ) {
        Icon(Icons.Rounded.Clear, contentDescription = "Clear Value")
    }
}