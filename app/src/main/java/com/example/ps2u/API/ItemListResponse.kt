package com.example.ps2u.API

data class ItemListResponse (
    val item_list: List<Item>,
    val returned: Int
)
data class Item (
    val item_id: String,
    val item_type_id: String,
    val item_category_id: String,
    val is_vehicle_weapon: String,

    val name: ItemName,
    val description: ItemDescription,

    val faction_id: String,
    val max_stack_size: String,
    val image_set_id: String,
    val image_id: String,
    val image_path: String,
    val is_default_attachment: String
)

data class ItemName (
    val de: String,
    val en: String,
    val es: String,
    val fr: String,
    val it: String,
    val tr: String
)

data class ItemDescription (
    val de: String,
    val en: String,
    val es: String,
    val fr: String,
    val it: String,
    val tr: String
)

