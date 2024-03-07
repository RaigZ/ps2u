package com.example.ps2u.RecyclerView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ps2u.R
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class ItemRVAdapter(private var context: Context, private var itemDataArray: Array<String>) : RecyclerView.Adapter<ItemRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRVAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemRVAdapter.ViewHolder, position: Int) {
        holder.itemText.text = itemDataArray[position]
        holder.itemText.typeface = ResourcesCompat.getFont(context, R.font.planetside2)

        if (position == 10) {
            holder.itemImg.visibility = View.VISIBLE
            holder.itemText.visibility = View.GONE
            Picasso.get().load(itemDataArray[10]).into(holder.itemImg)
        } else {
            holder.itemImg.visibility = View.GONE
            holder.itemText.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newSearchItems: Array<String>) {
        itemDataArray = newSearchItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemDataArray.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemText: TextView = itemView.findViewById(R.id.itemText)
        val itemImg: ShapeableImageView = itemView.findViewById(R.id.itemImage)
    }
}