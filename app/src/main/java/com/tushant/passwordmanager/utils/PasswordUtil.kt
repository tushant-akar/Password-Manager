package com.tushant.passwordmanager.utils


fun generateSuggestions(): List<String> {
    val suggestions = mutableListOf<String>()
    val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9') + listOf('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '+', '=')
    repeat(5) {
        val password = buildString {
            repeat(12) {
                append(chars.random())
            }
        }
        suggestions.add(password)
    }
    return suggestions
}
