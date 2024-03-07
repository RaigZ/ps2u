package com.example.ps2u

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class ItemAdapter(val context: Context, val data: Array<String>) : BaseAdapter() {
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): String {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var cView = convertView
        val inflater = LayoutInflater.from(context)
        if(convertView == null) {
            cView = inflater.inflate(R.layout.item_design, parent, false)
            val itemText = cView.findViewById<TextView>(R.id.itemText)
            itemText.typeface = ResourcesCompat.getFont(context, R.font.planetside2)
            val itemImg = cView.findViewById<ShapeableImageView>(R.id.itemImage)
            itemText.text = data[position]
            Picasso.get().load(data[10]).into(itemImg)

            if (position == 10) {
                itemImg.visibility = View.VISIBLE
                itemText.visibility = View.GONE
            } else {
                itemImg.visibility = View.GONE
                itemText.visibility = View.VISIBLE
            }
        }
        return cView
    }
}