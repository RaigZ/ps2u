package com.example.ps2u.RecyclerView

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ps2u.API.CharacterListResponse
import com.example.ps2u.API.Character
import com.example.ps2u.MainActivity
import com.example.ps2u.R

class RVAdapter(private var dataList: List<Character>) : RecyclerView.Adapter<RVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_design, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.char_id.text = "Character ID: ${item.character_id.toString()}"
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newSearchItems: CharacterListResponse) {
        dataList = newSearchItems.character_list
        notifyDataSetChanged()
    }

    override fun getItemCount() : Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val char_id: TextView = itemView.findViewById(R.id.char_id)

        init {
            itemView.setOnClickListener {
                val item = dataList[adapterPosition]
                Log.d("In RVAdapter", item.character_id)

                val intent = Intent(itemView.context, MainActivity::class.java)
                intent.putExtra("char_id", item.character_id)

                itemView.context.startActivity(intent)
            }
        }
    }
}