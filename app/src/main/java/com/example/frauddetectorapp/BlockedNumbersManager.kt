package com.example.frauddetectorapp

object BlockedNumbersManager {

    private val blockedNumbers = mutableSetOf<String>()

    fun add(number: String) {
        blockedNumbers.add(number)
    }

    fun isBlocked(number: String): Boolean {
        return blockedNumbers.contains(number)
    }

    fun remove(number: String) {
        blockedNumbers.remove(number)
    }

    fun getAll(): List<String> {
        return blockedNumbers.toList()
    }
}