package com.example.ps2u.WebSocket

data class Subscription(
    val subscription: SubscriptionEvent
)
data class SubscriptionEvent(
    val characterCount: Int,
    val eventNames: List<String>,
    val logicalAndCharactersWithWorlds: Boolean,
    val worlds: List<String>
)
