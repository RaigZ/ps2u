package com.example.ps2u.API

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PS2APIInterface {
    @GET("get/ps2:v2/character/")
    fun SearchCharacterByName (
        @Query("name.first") query: String,
        @Header("PS2API_SID") key: String = com.example.ps2u.BuildConfig.PS2_API_SID
    ): Call<CharacterListResponse>

    @GET("get/ps2:v2/character_name/")
    fun SearchCharacterNameByCollection (
        @Query("name.first") query: String,
        @Query("c:limit") limit: Int,
        @Header("PS2API_SID") key: String = com.example.ps2u.BuildConfig.PS2_API_SID
    ): Call<CharacterNameListResponse>


    @GET("get/ps2/item?")
    fun SearchItemByName (
        @Query("name.de") DE: String?,
        @Query("name.en") EN: String?,
        @Query("name.es") ES: String?,
        @Query("name.fr") FR: String?,
        @Query("name.it") IT: String?,
        @Query("name.tr") TR: String?,
        @Query("c:lang") lang: String,
        @Header("PS2API_SID") key: String = com.example.ps2u.BuildConfig.PS2_API_SID
    ): Call<ItemListResponse>
}