package com.example.ps2u

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.ps2u.API.PS2APIInterface
import com.example.ps2u.API.PS2APIService
import com.example.ps2u.RecyclerView.RVAdapter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ps2u.API.Character
import com.example.ps2u.API.CharacterListResponse
import com.example.ps2u.Fragments.CharacterFragment
import com.example.ps2u.Fragments.ItemFragment
import com.example.ps2u.Fragments.ServiceFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        sCharacterByName(PS2APIService.ps2API)

        val ServiceFragment = ServiceFragment()
        val ItemFragment = ItemFragment()
        val CharacterFragment = CharacterFragment()

        findViewById<BottomNavigationView>(R.id.bottom_nav).setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.ic_service_messages -> {
                    changeFragment(ServiceFragment)
                    true
                }
                R.id.ic_search_item -> {
                    changeFragment(ItemFragment)
                    true
                }
                R.id.ic_search_character -> {
                    changeFragment(CharacterFragment)
                    true
                }

                else -> {
                    false
                }
            }
        }
    }
    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fContainer, fragment)
            commit()
        }
    }

/* TEST PURPOSES */

//    private fun sCharacterByName(api: PS2APIInterface) {
//        val data = api.SearchCharacterByName("m0bile")
//        data.enqueue(object: Callback<CharacterListResponse>  {
//            override fun onResponse(
//                call: Call<CharacterListResponse>,
//                response: Response<CharacterListResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val apiResponse = response.body()
//                    Log.d("ApiResponse", "${apiResponse}")
//
//                } else {
//                    Log.e("ApiResponseError", response.errorBody()?.string() ?: "Unknown error")
//                }
//            }
//
//            override fun onFailure(call: Call<CharacterListResponse>, t: Throwable) {
//                Log.d("ApiFailure", "message" + t.message)
//            }
//        })
//    }
}