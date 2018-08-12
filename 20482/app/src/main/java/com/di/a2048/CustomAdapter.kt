package com.di.a2048

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game.*

class CustomAdapter : BaseAdapter {

    var img: IntArray
    var con: Context
    var name: Array<String>
    var activeBackground: IntArray

    private lateinit var inflator: LayoutInflater

    constructor(img: IntArray, con: Context, name: Array<String>, activeBackground: IntArray) : super() {
        this.img = img
        this.con = con
        this.name = name
        this.activeBackground = activeBackground
        inflator = con.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var holder: Holder = Holder()
        var rv: View
        rv = inflator.inflate(R.layout.custom_grid_tiles, null)
        holder.tv = rv.findViewById(R.id.tile_number_background) as TextView
        holder.iv = rv.findViewById(R.id.tile_face) as ImageView
        holder.tv.setText(name[p0].toString())
        holder.tv.setBackgroundResource(activeBackground[p0])
        holder.iv.setImageResource(img[p0])

/*        rv.setOnClickListener(object  : View.OnClickListener{
            override fun onClick(p0: View?) {
                Toast.makeText(con, holder.tv.text.toString(), Toast.LENGTH_SHORT).show()
            }
        })*/
        return rv
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return name.size
    }

    public class Holder{
        lateinit var tv: TextView
        lateinit var iv: ImageView
    }
}