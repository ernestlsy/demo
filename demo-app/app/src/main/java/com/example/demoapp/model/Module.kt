package com.example.demoapp.model

import com.example.demoapp.utils.Parser

/**
 * Represents the type of text provided for summarization.
 * @param moduleName Name of module e.g. Incident Report
 * @param fieldNames The names of fields to be generated, separate only by a comma
 */
class Module(val moduleName: String, val fieldNamesLiteral: String) {
    val fieldNames: List<String> = Parser.parseFields(fieldNamesLiteral)
}