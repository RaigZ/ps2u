package com.example.ps2u.API

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object PS2APIService {
    val ps2API by lazy {
        Retrofit.Builder()
            .baseUrl("https://census.daybreakgames.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(PS2APIInterface::class.java)
    }
}