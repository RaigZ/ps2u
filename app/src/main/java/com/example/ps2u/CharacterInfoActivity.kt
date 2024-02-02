package com.example.ps2u

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ps2u.API.CharacterListResponse
import com.example.ps2u.API.PS2APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        val intent = intent
        val name = intent.getStringExtra("character_name")
        val characterInfoList = findViewById<ListView>(R.id.characterInfoList)

        if(name == null) {
            Toast.makeText(this, "Unable to load character information.", Toast.LENGTH_LONG).show()
            finish()
        } else SearchCharacterInfo(name, characterInfoList, this)
    }

    inline fun <reified T: Any> convertListToString(list: List<T?>): List<String> {
        return list.map { it.toString() }
    }

    private fun SearchCharacterInfo(name: String, listView: ListView, context: Context) {
        val data = PS2APIService.ps2API.SearchCharacterByName(name)
        data.enqueue(object: Callback<CharacterListResponse> {
            override fun onResponse(
                call: Call<CharacterListResponse>,
                response: Response<CharacterListResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val labels =
                        listOf("$name\'s Details", "Returned")
                    val characterData = listOf(
                        apiResponse?.character_list,
                        apiResponse?.returned
                    ).mapNotNull { it }

                    var characterInfoListCollection: Any? = characterData[0]
                    val returned : Any = characterData[1]


                    characterInfoListCollection = convertListToString<Any>(
                        characterInfoListCollection as List<*>
                    )

                    characterInfoListCollection = characterInfoListCollection.plus(returned.toString())

                    listView.adapter = CharacterActivityAdapter(context, characterInfoListCollection, labels)
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

    private class CharacterActivityAdapter(context: Context, cList: List<String>, labels: List<String>): BaseAdapter() {
        private val characterContext: Context
        private val characterList: List<String>
        private val characterLabels: List<String>
                init {
                    characterContext = context
                    characterList = cList
                    characterLabels = labels
                }

        override fun getCount(): Int {
            return characterList.size
        }

        override fun getItem(position: Int): Any? {
            return null
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(characterContext)
            val itemRow = layoutInflater.inflate(R.layout.character_row, parent, false)

            val tvCharacterLabel = itemRow.findViewById<TextView>(R.id.tvCharacterLabel)
            val tvCharacterInfo = itemRow.findViewById<TextView>(R.id.tvCharacterInfo)

            tvCharacterLabel.text = characterLabels[position]
            tvCharacterInfo.text = characterList[position]

            return itemRow
        }
    }
}