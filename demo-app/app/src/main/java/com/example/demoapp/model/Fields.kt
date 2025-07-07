package com.example.demoapp.model

class Fields(
    val fieldNames: List<String> = listOf<String>(),
    val values: List<String> = listOf<String>()
) {
    fun editValue(index: Int, newValue: String): Fields {
        val temp = values.toMutableList()
        temp[index] = newValue
        return Fields(fieldNames, temp)
    }
}