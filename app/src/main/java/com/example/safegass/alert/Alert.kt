package com.example.safegass.alert

data class Alert(
    val id: Int,
    val title: String,
    val description: String,
    val source: String,
    val timeAgo: String,
    val level: String // Danger, Warning, Info
)
