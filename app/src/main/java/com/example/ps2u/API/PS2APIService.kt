package com.example.ps2u.API

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val URL = "https://census.daybreakgames.com"
object PS2APIService {
    val ps2API by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(PS2APIInterface::class.java)
    }
}