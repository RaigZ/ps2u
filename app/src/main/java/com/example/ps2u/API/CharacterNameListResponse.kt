package com.example.ps2u.API

data class CharacterNameListResponse (
    val character_name_list: List<CharacterNameCollection>,
    val returned: Int
)

data class CharacterNameCollection (
    val name: CharacterFirstName
)

data class CharacterFirstName (
    val first: String
)