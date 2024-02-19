package com.example.ps2u.WebSocket

data class Online(
    val online: ServerOnlineEvent,
    val service: String,
    val timestamp: String,
    val type: String
)
data class ServerOnlineEvent(
    val EventServerEndpoint_Cobalt_13: String,
    val EventServerEndpoint_Connery_1: String,
    val EventServerEndpoint_Emerald_17: String,
    val EventServerEndpoint_Jaeger_19: String,
    val EventServerEndpoint_Miller_10: String,
    val EventServerEndpoint_Soltech_40: String
)
