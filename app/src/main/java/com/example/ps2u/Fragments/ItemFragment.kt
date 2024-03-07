package com.example.ps2u.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ps2u.API.Item
import com.example.ps2u.API.ItemListResponse
import com.example.ps2u.API.PS2APIService
import com.example.ps2u.API.URL
import com.example.ps2u.KeysArrayAdapter
import com.example.ps2u.R
import com.example.ps2u.RecyclerView.ItemRVAdapter
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
 * Use the [ItemFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ItemFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var root: View
    private lateinit var searchItemFieldLayout: TextInputLayout
    private lateinit var searchItemFieldContent: TextInputEditText
    private lateinit var spLangOpt: Spinner
//    private lateinit var itemListView: ListView
    private lateinit var itemRecView: RecyclerView
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
        root = inflater.inflate(R.layout.fragment_item, container, false)

        val api = PS2APIService.ps2API
        val lang = mapOf(
            "en" to "English",
            "fr" to "French",
            "de" to "German",
            "it" to "Italian",
            "es" to "Spanish",
            "tr" to "Turkish",
        )
        val itemKeyArrayAdapter = KeysArrayAdapter(context, lang)

        val curLocale = resources.configuration.locales
        val curLocaleLang = curLocale.toLanguageTags()
        var curLangSelection = String()
        var itemDetails = emptyArray<String>()
        var itemAdapter = ItemRVAdapter(requireContext(), emptyArray<String>())

        searchItemFieldLayout = root.findViewById(R.id.searchItemLayout)
        searchItemFieldContent = root.findViewById(R.id.itemSearchEntry)
        spLangOpt = root.findViewById(R.id.itemLangSpinner)
        itemRecView = root.findViewById(R.id.itemRecyclerView)
        itemRecView.layoutManager = LinearLayoutManager(requireContext())
//        itemListView = root.findViewById(R.id.itemListView)
        spLangOpt.adapter = itemKeyArrayAdapter
        itemRecView.adapter = itemAdapter


        searchItemFieldLayout.typeface = ResourcesCompat.getFont(requireContext(), R.font.planetside2)
        searchItemFieldContent.typeface = ResourcesCompat.getFont(requireContext(), R.font.planetside2)

        when(curLocaleLang.substring(0, 2)) {
            lang.keys.toList()[0] -> spLangOpt.setSelection(0)
            lang.keys.toList()[1] -> spLangOpt.setSelection(1)
            lang.keys.toList()[2] -> spLangOpt.setSelection(2)
            lang.keys.toList()[3] -> spLangOpt.setSelection(3)
            lang.keys.toList()[4] -> spLangOpt.setSelection(4)
            lang.keys.toList()[5] -> spLangOpt.setSelection(5)
            else -> {
                spLangOpt.setSelection(0)
                searchItemFieldLayout.hint = resources.getString(R.string.search_item_hint_opt_en)
            }
        }

        spLangOpt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                curLangSelection = lang.keys.toList()[position]
                when(position) {
                    0 -> searchItemFieldLayout.hint = resources.getString(R.string.search_item_hint_opt_en)
                    1 -> searchItemFieldLayout.hint = resources.getString(R.string.search_item_hint_opt_fr)
                    2 -> searchItemFieldLayout.hint = resources.getString(R.string.search_item_hint_opt_de)
                    3 -> searchItemFieldLayout.hint = resources.getString(R.string.search_item_hint_opt_it)
                    4 -> searchItemFieldLayout.hint = resources.getString(R.string.search_item_hint_opt_es)
                    5 -> searchItemFieldLayout.hint = resources.getString(R.string.search_item_hint_opt_tr)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        searchItemFieldContent.setOnEditorActionListener { _, action, _ ->
            Toast.makeText(context, ("Search: " + searchItemFieldContent.text?.toString()), Toast.LENGTH_LONG).show()
            if(action == EditorInfo.IME_ACTION_DONE) {
               when(curLangSelection) {
                       lang.keys.toList()[0] -> api.SearchItemByName(null, ("^" + searchItemFieldContent.text?.toString()?.replace("[^[^.=/\\\\]*\$]".toRegex(), "")), null, null, null, null, curLangSelection)
                       lang.keys.toList()[1] -> api.SearchItemByName(null, null, null, ("^" + searchItemFieldContent.text?.toString()?.replace("[^[^.=/\\\\]*\$]".toRegex(), "")), null, null, curLangSelection)
                       lang.keys.toList()[2] -> api.SearchItemByName(("^" + searchItemFieldContent.text?.toString()?.replace("[^[^.=/\\\\]*\$]".toRegex(), "")), null, null, null, null, null, curLangSelection)
                       lang.keys.toList()[3] -> api.SearchItemByName(null, null, null, null, ("^" + searchItemFieldContent.text?.toString()?.replace("[^[^.=/\\\\]*\$]".toRegex(), "")), null, curLangSelection)
                       lang.keys.toList()[4] -> api.SearchItemByName(null, null, ("^" + searchItemFieldContent.text?.toString()?.replace("[^[^.=/\\\\]*\$]".toRegex(), "")), null, null, null, curLangSelection)
                       lang.keys.toList()[5] -> api.SearchItemByName(null, null, null, null, null, ("^" + searchItemFieldContent.text?.toString()?.replace("[^[^.=/\\\\]*\$]".toRegex(), "")), curLangSelection)
                   else -> {
                       api.SearchItemByName(null, ("^" + searchItemFieldContent.text?.toString()?.replace("[^[^.=/\\\\]*\$]".toRegex(), "")), null, null, null, null, curLangSelection)
                   }
               }.enqueue(object : Callback<ItemListResponse> {
                    override fun onResponse(
                        call: Call<ItemListResponse>,
                        response: Response<ItemListResponse>
                    ) {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            Log.d("ApiResponse", "$apiResponse")
                            apiResponse?.let {
                                val itemData = listOf(
                                    apiResponse.item_list,
                                    apiResponse.returned
                                ).map { it }

                                val itemList = itemData.filterIsInstance<List<Item>>().firstOrNull()

                                if (itemList != null) {

                                    val returned : Any = itemData[1]

                                    for (item in itemList) {
                                        val name = when (curLangSelection) {
                                            lang.keys.toList()[0] -> item.name.en
                                            lang.keys.toList()[1] -> item.name.fr
                                            lang.keys.toList()[2] -> item.name.de
                                            lang.keys.toList()[3] -> item.name.it
                                            lang.keys.toList()[4] -> item.name.es
                                            lang.keys.toList()[5] -> item.name.tr
                                            else -> {
                                                item.name.en
                                            }
                                        }

                                        val description = when (curLangSelection) {
                                            lang.keys.toList()[0] -> item.description.en
                                            lang.keys.toList()[1] -> item.description.fr
                                            lang.keys.toList()[2] -> item.description.de
                                            lang.keys.toList()[3] -> item.description.it
                                            lang.keys.toList()[4] -> item.description.es
                                            lang.keys.toList()[5] -> item.description.tr
                                            else -> {
                                                item.description.en
                                            }
                                        }

                                        val isVehicleWeapon = if (item.is_vehicle_weapon == "1") "Yes" else "No"
                                        val isDefaultAttachment = if (item.is_default_attachment == "1")  "Yes" else "No"

                                        itemDetails = arrayOf(
                                            "ID: ${item.item_id}",
                                            "Type ID: ${item.item_type_id}",
                                            "Category ID: ${item.item_category_id}",
                                            "Vehicle Weapon: $isVehicleWeapon",
                                            "Name: $name",
                                            "Description: $description",
                                            "Faction ID: ${item.faction_id}",
                                            "Max Stack Size: ${item.max_stack_size}",
                                            "Image Set ID: ${item.image_set_id}",
                                            "Image ID: ${item.image_id}",
                                            URL + item.image_path,
                                            "Default Attachment: $isDefaultAttachment",
                                            "Returned: $returned"
                                        )
                                    }
                                    itemAdapter.setData(itemDetails)
//                                    itemListView.adapter = ItemAdapter(requireContext(), itemDetails)
                                }
                            }
                        } else {
                            Log.e(
                                "ApiResponseError",
                                response.errorBody()?.string() ?: "Unknown error"
                            )
                        }
                    }

                    override fun onFailure(call: Call<ItemListResponse>, t: Throwable) {
                        Log.d("ApiFailure", "message" + t.message)
                    }
               })
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
         * @return A new instance of fragment ItemFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ItemFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}