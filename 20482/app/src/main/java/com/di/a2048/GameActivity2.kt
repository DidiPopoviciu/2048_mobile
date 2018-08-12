package com.di.a2048

import android.animation.ObjectAnimator
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity2 : AppCompatActivity() {

    var con: Context = this
    var img: IntArray = intArrayOf(
            R.drawable.ic_ic_face_0_v3, R.drawable.ic_ic_face_0_v3,
            R.drawable.ic_ic_face_0_v3, R.drawable.ic_ic_face_0_v3,
            R.drawable.ic_ic_face_0_v3, R.drawable.ic_ic_face_0_v3,
            R.drawable.ic_ic_face_0_v3, R.drawable.ic_ic_face_0_v3,
            R.drawable.ic_ic_face_0_v3, R.drawable.ic_ic_face_0_v3,
            R.drawable.ic_ic_face_0_v3, R.drawable.ic_ic_face_0_v3,
            R.drawable.ic_ic_face_0_v3, R.drawable.ic_ic_face_0_v3,
            R.drawable.ic_ic_face_0_v3, R.drawable.ic_ic_face_0_v3)
    var name: Array<String> = arrayOf("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")
    lateinit var gv: GridView
    lateinit var cl: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game2)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        gv = findViewById(R.id.tiles_background) as GridView
        cl = CustomAdapter(img, con, name, IntArray(0))
        gv.adapter = cl






        /*val gridview: GridView = findViewById(R.id.tiles_background)
        gridview.adapter = ImageAdapter(this)


        gridview.onItemClickListener =
                AdapterView.OnItemClickListener { parent, v, position, id ->
                    Toast.makeText(this, "$position", Toast.LENGTH_SHORT).show()
                }*/

/*        val recyclerView = findViewById(R.id.recyclerView) as RecyclerView

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        val users = ArrayList<TileUnitClass>()

//        recyclerView.layoutParams = ViewGroup.LayoutParams(235, 264)

        val createViewOnCustomPosition = ObjectAnimator.ofFloat(recyclerView, "translationX", 240f)
        createViewOnCustomPosition.duration = 1000
        createViewOnCustomPosition.start()

        users.add(TileUnitClass("2"))

        val adapter = TileUnitAdapter(users)

        recyclerView.adapter = adapter*/
    }
}
