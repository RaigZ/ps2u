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
}