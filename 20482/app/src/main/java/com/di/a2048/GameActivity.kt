package com.di.a2048

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game.*
import android.text.method.Touch.onTouchEvent
import android.view.MotionEvent
import android.view.View.OnTouchListener

import android.view.View
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.activity_main.*


class GameActivity() : AppCompatActivity()
    {
        private var x1 = 0f
        private var y1 = 0f

        var globalX = 0
        var globalY = 0

        var endy_x = 0
        var endy_y = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_game)
        supportActionBar?.hide()

        var first = true

        var listener = View.OnTouchListener(function = {view, motionEvent -> if (motionEvent.action == MotionEvent.ACTION_MOVE) {
            view.y = motionEvent.rawY - view.height
            view.x = motionEvent.rawX - view.width/2
            if (first){
                first = false
                globalX = endyPowerUp.x.toInt()
                globalY = endyPowerUp.y.toInt()

                Toast.makeText(this, "x = $globalX, y = $globalY", Toast.LENGTH_SHORT).show();
            }

            endyPowerUp.background = ContextCompat.getDrawable(this, R.drawable.ic_ic_endy_powerup)

        }
            true

        })

           endyPowerUp.setOnTouchListener(listener)



        var returnEndy = View.OnTouchListener(function = {view, motionEvent -> if (motionEvent.action == MotionEvent.ACTION_UP) {

            Toast.makeText(this, "Endy should return to $globalX and $globalY", Toast.LENGTH_SHORT).show();

            endyPowerUp.x = globalX.toFloat()
            endyPowerUp.y = globalY.toFloat()

        }
            true
        })

        restartGame.setOnTouchListener(returnEndy)




        var listenerChangeColor = View.OnTouchListener(function = {view, motionEvent -> if(motionEvent.action == MotionEvent.ACTION_DOWN) {
            square1b1.setTextColor(-0xffff01)
        }
            true
        })

        square1b1.setOnTouchListener(listenerChangeColor)

        closeGame.setOnClickListener{
            val intent = Intent(this, GameActivity::class.java)
            super.finish()

            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        }

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

    override fun onStart() {
        super.onStart()

        endy_x = endyPowerUp.x.toInt()
        endy_y = endyPowerUp.y.toInt()

        Toast.makeText(this, "endy_x = $endy_x & endy_y = $endy_y", Toast.LENGTH_SHORT).show();

    }

    fun saveEndyPosition() {
        globalX = endyPowerUp.x.toInt()
        globalY = endyPowerUp.y.toInt()
    }

    // onTouchEvent () method gets called when User performs any touch event on screen
    // Method to handle touch event like left to right swap and right to left swap
    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
        // when user first touches the screen we get x and y coordinate
//            MotionEvent.ACTION_DOWN -> {
//                x1 = event.x
//                y1 = event.y
//            }
            MotionEvent.ACTION_UP -> {
                val x2 = event.x
                val y2 = event.y

                val minDistance = 200
                var moved = false

                //if left to right sweep event on screen
                if (x1 < x2 && x2 - x1 > minDistance) {
                    Toast.makeText(this, "Left to Right Swap Performed", Toast.LENGTH_SHORT).show();
//                        moved = game.actionMove(Moves.Right)
                }

                // if right to left sweep event on screen
                if (x1 > x2 && x1 - x2 > minDistance) {
                    Toast.makeText(this, "Right to Left Swap Performed", Toast.LENGTH_SHORT).show();

//                        moved = game.actionMove(Moves.Left)
                }

                // if UP to Down sweep event on screen
                if (y1 < y2 && y2 - y1 > minDistance) {
                    Toast.makeText(this, "UP to Down Swap Performed", Toast.LENGTH_SHORT).show();
//                        moved = game.actionMove(Moves.Down)
                }

                //if Down to UP sweep event on screen
                if (y1 > y2 && y1 - y2 > minDistance) {
                    Toast.makeText(this, "Down to UP Swap Performed", Toast.LENGTH_SHORT).show();
//                        moved = game.actionMove(Moves.Up)
                }

            }
        }
        return true
    }


    }


