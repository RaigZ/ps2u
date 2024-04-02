package com.example.ps2u

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ps2u.API.Character
import com.example.ps2u.API.CharacterListResponse
import com.example.ps2u.API.PS2APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)
        findViewById<Button>(R.id.back).setOnClickListener {
            finish()
        }
        val intent = intent
        val name = intent.getStringExtra("character_name")
        val characterInfoList = findViewById<ListView>(R.id.characterInfoList)

        if(name == null)
            Toast.makeText(this, "Unable to load character information.", Toast.LENGTH_LONG).show()
        else SearchCharacterInfo(name, characterInfoList, this)
    }

    private fun SearchCharacterInfo(name: String, listView: ListView, context: Context) {
        val data = PS2APIService.ps2API.SearchCharacterByName(name)
        var characterDetails = emptyArray<String>()
        data.enqueue(object: Callback<CharacterListResponse> {
            override fun onResponse(
                call: Call<CharacterListResponse>,
                response: Response<CharacterListResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val labels =
                        listOf("Character ID", "Character Name", "Faction ID",
                            "Head ID", "Title ID", "Creation",
                            "Creation Date", "Last Save", "Last Save Date",
                            "Last Login", "Last Login Date", "Login Count",
                            "Minutes Played", "Certs Earned", "Certs Gifted",
                            "Certs Spent", "Certs Available", "Percent to Next Certs",
                            "Percent to Next Battle Rank", "Battle Rank", "Profile ID",
                            "Daily Ribbon Count", "Daily Ribbon Time", "Daily Ribbon Date",
                            "Prestige Level", "Returned"
                        )
                    val characterData = listOf(
                        apiResponse?.character_list,
                        apiResponse?.returned
                    ).mapNotNull { it }

                    val characterInfoListCollection = characterData.filterIsInstance<List<Character>>().firstOrNull()

                    if (characterInfoListCollection != null) {

                        val returned : Any = characterData[1]

                        for (character in characterInfoListCollection) {
                           characterDetails = arrayOf(character.character_id, character.name.first, character.faction_id,
                               character.head_id, character.title_id, character.times.creation, character.times.creation_date,
                               character.times.last_save, character.times.last_save_date, character.times.last_login,
                               character.times.last_login_date, character.times.login_count, character.times.minutes_played,
                               character.certs.earned_points, character.certs.gifted_points, character.certs.spent_points,
                               character.certs.available_points, character.certs.percent_to_next, character.battle_rank.percent_to_next,
                               character.battle_rank.value, character.profile_id, character.daily_ribbon.count,
                               character.daily_ribbon.time, character.daily_ribbon.date, character.prestige_level,
                               returned.toString()
                           )
                        }
                    }

                    listView.adapter = CharacterActivityAdapter(context, characterDetails, labels)
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

    private class CharacterActivityAdapter(context: Context, cList: Array<String>, labels: List<String>): BaseAdapter() {
        private val characterContext: Context
        private val characterList: Array<String>
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
            val characterRow = layoutInflater.inflate(R.layout.character_row, parent, false)

            val tvCharacterLabel = characterRow.findViewById<TextView>(R.id.tvCharacterLabel)
            val tvCharacterInfo = characterRow.findViewById<TextView>(R.id.tvCharacterInfo)

            tvCharacterLabel.text = characterLabels[position]
            tvCharacterInfo.text = characterList[position]

            return characterRow
        }
    }
}