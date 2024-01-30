package com.example.ps2u

import androidx.appcompat.app.AppCompatActivity
import com.example.ps2u.API.PS2APIInterface
import com.example.ps2u.API.PS2APIService
import com.example.ps2u.RecyclerView.RVAdapter
import android.os.Bundle
import android.util.Log
import com.example.ps2u.API.CharacterListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val PS2API = PS2APIService.ps2API
        sCharacterByName(PS2API)
    }

    private fun sCharacterByName(api: PS2APIInterface) {
        val data = api.SearchCharacterByName("m0bile")
        data.enqueue(object: Callback<CharacterListResponse>  {
            override fun onResponse(
                call: Call<CharacterListResponse>,
                response: Response<CharacterListResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.d("ApiResponse", "${apiResponse}")

                } else {
                    Log.e("ApiResponseError", response.errorBody()?.string() ?: "Unknown error")
                }
            }

            override fun onFailure(call: Call<CharacterListResponse>, t: Throwable) {
                Log.d("ApiFailure", "message" + t.message)
            }
        })
    }
}