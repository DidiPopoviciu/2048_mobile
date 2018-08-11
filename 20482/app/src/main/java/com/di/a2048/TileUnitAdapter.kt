package com.di.a2048

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.di.a2048.R.layout.custom_grid_tiles

class TileUnitAdapter(val numberList: ArrayList<TileUnitClass>) : RecyclerView.Adapter<TileUnitAdapter.ViewHolder>() {

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val user: TileUnitClass = numberList[p1]

        p0?.textViewNumber?.text = user.number.toString()
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder{
        val v = LayoutInflater.from(p0?.context).inflate(custom_grid_tiles, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return numberList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNumber = itemView.findViewById(R.id.tile_number_background) as TextView
    }

}