package com.example.ps2u.WebSocket

data class FacilityControlPayload(
    val duration_held: String,
    val event_name: String,
    val facility_id: String,
    val new_faction_id: String,
    val old_faction_id: String,
    val outfit_id: String,
    val timestamp: String,
    val world_id: String,
    val zone_id: String
)
data class FacilityControlEvent(
    val payload: FacilityControlPayload,
    val service: String,
    val serviceMessage: String
)
