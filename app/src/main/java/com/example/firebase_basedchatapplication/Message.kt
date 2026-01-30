package com.example.firebase_basedchatapplication

data class Message(
    val text: String = "",
    val senderEmail: String = "",
    val senderName: String = "",
    val timestamp: Long = 0,
    val senderId: String = ""
)
