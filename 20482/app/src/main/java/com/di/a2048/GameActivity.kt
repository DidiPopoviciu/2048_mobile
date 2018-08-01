package com.di.a2048

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_game)
        supportActionBar?.hide()

        var listener = View.OnTouchListener(function = {view, motionEvent -> if (motionEvent.action == MotionEvent.ACTION_MOVE) {
            view.y = motionEvent.rawY - view.height
            view.x = motionEvent.rawX - view.width/2

        }
        true

        })

        endyPowerUp.setOnTouchListener(listener)

//        var listenerUp = View.OnTouchListener(function = {view, motionEvent -> if (motionEvent.action == MotionEvent.ACTION_UP) {
//            view.visibility = View.INVISIBLE
//
//        }
//            true
//
//        })
//
//        endyPowerUp.setOnTouchListener(listenerUp)

    }
}
