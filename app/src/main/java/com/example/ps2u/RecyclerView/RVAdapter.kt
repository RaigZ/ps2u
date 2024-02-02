package com.example.ps2u.RecyclerView

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ps2u.API.CharacterNameCollection
import com.example.ps2u.API.CharacterNameListResponse
import com.example.ps2u.CharacterInfoActivity
import com.example.ps2u.R

class RVAdapter(private var dataList: List<CharacterNameCollection>) : RecyclerView.Adapter<RVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_design, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.char_name.text = item.name.first
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newSearchItems: CharacterNameListResponse) {
        dataList = newSearchItems.character_name_list
        notifyDataSetChanged()
    }

    override fun getItemCount() : Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val char_name: TextView = itemView.findViewById(R.id.char_name)

        init {
            itemView.setOnClickListener {
                val item = dataList[adapterPosition]
                Log.d("In RVAdapter", item.name.first)

                val intent = Intent(itemView.context, CharacterInfoActivity::class.java)
                intent.putExtra("character_name", item.name.first)

                itemView.context.startActivity(intent)
            }
        }
    }
}