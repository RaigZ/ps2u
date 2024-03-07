package com.example.ps2u.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ps2u.API.CharacterNameListResponse
import com.example.ps2u.API.PS2APIInterface
import com.example.ps2u.API.PS2APIService
import com.example.ps2u.R
import com.example.ps2u.RecyclerView.RVAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CharacterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharacterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchCharacterFieldLayout: TextInputLayout
    private lateinit var searchCharacterFieldContent: TextInputEditText
    private lateinit var characterAdapter: RVAdapter
    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_character, container, false)
        recyclerView = root.findViewById(R.id.characterRecyclerView)
        searchCharacterFieldLayout = root.findViewById(R.id.searchCharacterLayout)
        searchCharacterFieldContent = root.findViewById(R.id.characterSearchEntry)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        var characterAdapter = RVAdapter(emptyList())
        recyclerView.adapter = characterAdapter

        searchCharacterFieldLayout.typeface = ResourcesCompat.getFont(requireContext(), R.font.planetside2)
        searchCharacterFieldContent.typeface = ResourcesCompat.getFont(requireContext(), R.font.planetside2)

        val whitespaceOccurrence = Regex("[/\\s+|^\$/\n]")

        fun sCharacterNameByCollection(api: PS2APIInterface, adapter: RVAdapter) {
            if (searchCharacterFieldContent.text?.toString()
                    ?.let { whitespaceOccurrence.containsMatchIn(it) } == true
                || searchCharacterFieldContent.text?.toString() == ""
            ) {
                Toast.makeText(context,"Cannot perform null search.", Toast.LENGTH_LONG).show()
            } else {
                val data = api.SearchCharacterNameByCollection(
                    ("^" + searchCharacterFieldContent.text?.toString()
                        ?.replace("[\\s\\p{Z}]".toRegex(), "")), 10
                )
                data.enqueue(object : Callback<CharacterNameListResponse> {
                    override fun onResponse(
                        call: Call<CharacterNameListResponse>,
                        response: Response<CharacterNameListResponse>
                    ) {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            Log.d("ApiResponse", "${apiResponse}")
                            apiResponse?.let {
                                adapter.setData(it)
                            }
                        } else {
                            Log.e(
                                "ApiResponseError",
                                response.errorBody()?.string() ?: "Unknown error"
                            )
                        }
                    }

                    override fun onFailure(call: Call<CharacterNameListResponse>, t: Throwable) {
                        Log.d("ApiFailure", "message" + t.message)
                    }
                })
            }
        }

        searchCharacterFieldContent.setOnEditorActionListener { _, action, _ ->
            Toast.makeText(context, ("Search: " + searchCharacterFieldContent.text?.toString()), Toast.LENGTH_LONG).show()
            if(action == EditorInfo.IME_ACTION_DONE) {
                sCharacterNameByCollection(PS2APIService.ps2API, characterAdapter)
                return@setOnEditorActionListener true
            }
            false
        }
        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CharacterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CharacterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}