package com.example.ps2u.API

data class CharacterListResponse (
    val character_list: List<Character>,
    val returned: Int
)

data class Character (
    val character_id: String,
    val name: CharacterName,
    val faction_id: String,
    val head_id: String,
    val title_id: String,
    val times: Time,
    val certs: Certs,
    val battle_rank: BattleRank,
    val profile_id: String,
    val daily_ribbon: DailyRibbon,
    val prestige_level: String
)

data class CharacterName (
    val first: String,
    val first_lower: String
)

data class Time (
    val creation : String,
    val creation_date: String,
    val last_save: String,
    val last_save_date: String,
    val last_login: String,
    val last_login_date: String,
    val login_count: String,
    val minutes_played: String
)

data class Certs (
    val earned_points: String,
    val gifted_points: String,
    val spent_points: String,
    val available_points: String,
    val percent_to_next: String
)

data class BattleRank (
    val percent_to_next: String,
    val value: String
)

data class DailyRibbon (
    val count: String,
    val time: String,
    val date: String
)