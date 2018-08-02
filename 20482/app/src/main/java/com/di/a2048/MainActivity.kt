package com.di.a2048

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Window
import android.view.WindowManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_dialog.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

//        requestWindowFeature(Window.FEATURE_ACTION_BAR)
//        actionBar.hide()

        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val width_of_screen = displayMetrics.widthPixels
        val height_of_screen = displayMetrics.heightPixels

        var logo_height = 0

//        logo_height = (20 * height_of_screen)/100
//        textV.text = logo_height.toString()
//        if(logo_height < 150)
//            imageView4.layoutParams.height = logo_height
//        else
//            imageView4.layoutParams.height = 150

        var logo_width = 0

        logo_width = (37 * height_of_screen)/100
        textV.text = logo_width.toString()
        imageView4.layoutParams.width = logo_width
        imageView4.layoutParams.height = (281 * logo_width)/720


        startLevel.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }



        openDialog.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)

            val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)

            val mAlertDialog = mBuilder.show()

            mDialogView.restoreVitality.setOnClickListener{
                mAlertDialog.dismiss()
            }

            mDialogView.skipVitality.setOnClickListener{
                mAlertDialog.dismiss()
            }

        }

    }
}
