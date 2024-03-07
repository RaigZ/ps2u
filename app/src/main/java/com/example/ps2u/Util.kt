package com.example.ps2u

import android.content.Context
import android.widget.ArrayAdapter
import com.example.ps2u.API.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Util {
    inline fun <reified T> Gson.fromJson(json: Map<*, *>): T {
        val jsonString = this.toJson(json)
        return this.fromJson(jsonString, object : TypeToken<T>() {}.type)
    }

    fun iterateListFormat(list: List<String>, stringBuilder: StringBuilder) {
        list.forEachIndexed { index, it ->
            if ((index < list.size - 1))
                stringBuilder.append("$it, ")
            else if (index >= list.size - 1)
                stringBuilder.append(it + '\n')
        }
    }

    inline fun <reified T: Any> convertListToString(list: List<T?>): List<String> {
        return list.map { it.toString() }
    }
}

class KeysArrayAdapter<K>(context: Context?, map: Map<K, *>) :
    ArrayAdapter<K>(context!!, android.R.layout.simple_spinner_dropdown_item, map.keys.toList())