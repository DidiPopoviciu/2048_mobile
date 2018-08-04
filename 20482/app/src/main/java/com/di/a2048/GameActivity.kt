package com.di.a2048

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game.*
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.Window
import android.view.WindowManager
import android.animation.ObjectAnimator


class GameActivity() : AppCompatActivity()
    {
        private var x1 = 0f
        private var y1 = 0f

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)


            window.requestFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
            setContentView(R.layout.activity_game)
            supportActionBar?.hide()

            var listener = OnTouchListener(function = {view, motionEvent ->

                if (motionEvent.action == MotionEvent.ACTION_MOVE)
                {
                    view.y = motionEvent.rawY - view.height
                    view.x = motionEvent.rawX - view.width/2

                    endyPowerUp.background = ContextCompat.getDrawable(this, R.drawable.ic_ic_endy_powerup)

                }
                true

            })

            endyPowerUp.setOnTouchListener(listener)



            var returnEndy = OnTouchListener(function = {view, motionEvent -> if (motionEvent.action == MotionEvent.ACTION_UP) {

//                val path = Path()
//                val pathInterpolator = PathInterpolator(PathInterpolatorCompat.)

                val animationEndyX = ObjectAnimator.ofFloat(endyPowerUp, "translationX", 0f)
                animationEndyX.duration = 500

//                animationEndyX.setInterpolator(pathInterpolator)
                animationEndyX.start()

                val animationEndyY = ObjectAnimator.ofFloat(endyPowerUp, "translationY", 0f)
                animationEndyY.duration = 500
                animationEndyY.start()

            }
                true
            })

            restartGame.setOnTouchListener(returnEndy)

            var listenerChangeColor = OnTouchListener(function = {view, motionEvent -> if(motionEvent.action == MotionEvent.ACTION_DOWN) {
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

        }

        override fun onTouchEvent(event: MotionEvent): Boolean {

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    x1 = event.x
                    y1 = event.y
                }

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


