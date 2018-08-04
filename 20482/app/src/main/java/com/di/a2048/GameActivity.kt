package com.di.a2048

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game.*
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import java.util.*
import java.util.logging.Logger


class GameActivity() : AppCompatActivity()
    {
        val Log = Logger.getLogger(MainActivity::class.java.name)

        private var x1 = 0f
        private var y1 = 0f

        var valuesMatrix = arrayOf(intArrayOf(0, 0, 0, 0),
                                    intArrayOf(0, 0, 0, 0),
                                    intArrayOf(0, 0, 0, 0),
                                    intArrayOf(0, 0, 0, 0)
        )

        var textViewsMatrix = arrayOf<Array<TextView>>()

        var endy_x = 0
        var endy_y = 0

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)


            window.requestFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
            setContentView(R.layout.activity_game)
            supportActionBar?.hide()

            textViewsMatrix = arrayOf(arrayOf(element1, element2, element3, element4),
                    arrayOf(element5, element6, element7, element8),
                    arrayOf(element9, element10, element11, element12),
                    arrayOf(element13, element14, element15, element16)
            )
            initMatrix(textViewsMatrix, valuesMatrix, this)

            generateNewElement(valuesMatrix, textViewsMatrix, this)
            generateNewElement(valuesMatrix, textViewsMatrix, this)

            var listener = View.OnTouchListener(function = { view, motionEvent ->

                if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                    view.y = motionEvent.rawY - view.height
                    view.x = motionEvent.rawX - view.width / 2

                    endyPowerUp.background = ContextCompat.getDrawable(this, R.drawable.ic_ic_endy_powerup)
                }
                true
            })
            endyPowerUp.setOnTouchListener(listener)



            var returnEndy = View.OnTouchListener(function = { view, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP) {



                initMatrix(textViewsMatrix, valuesMatrix, this)

                val animationEndyX = ObjectAnimator.ofFloat(endyPowerUp, "translationX", 0f)
                animationEndyX.duration = 500

                animationEndyX.start()

                val animationEndyY = ObjectAnimator.ofFloat(endyPowerUp, "translationY", 0f)
                animationEndyY.duration = 500
                animationEndyY.start()

                }
                true
            })
            restartGame.setOnTouchListener(returnEndy)


            var listenerChangeColor = View.OnTouchListener(function = { view, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    element1.setTextColor(-0xffff01)
                }
                true
            })
            element1.setOnTouchListener(listenerChangeColor)

            closeGame.setOnClickListener {
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
                        generateNewElement(valuesMatrix, textViewsMatrix, this)
                        Log.warning("Left to Right Swap Performed")

                        //                        moved = game.actionMove(Moves.Right)
                    }

                    // if right to left sweep event on screen
                    if (x1 > x2 && x1 - x2 > minDistance) {
                        generateNewElement(valuesMatrix, textViewsMatrix, this)
                        Log.warning("Right to Left Swap Performed")
                        //                        moved = game.actionMove(Moves.Left)
                    }

                    // if UP to Down sweep event on screen
                    if (y1 < y2 && y2 - y1 > minDistance) {
                        generateNewElement(valuesMatrix, textViewsMatrix, this)
                        Log.warning("UP to Down Swap Performed")
                        //                        moved = game.actionMove(Moves.Down)
                    }

                    //if Down to UP sweep event on screen
                    if (y1 > y2 && y1 - y2 > minDistance) {
                        generateNewElement(valuesMatrix, textViewsMatrix, this)
                        Log.warning("Down to UP Swap Performedd")
                        //                        moved = game.actionMove(Moves.Up)
                    }

                    Log.warning("-----------------------------------")

                }
            }
            return true
        }


        private fun initMatrix(buttons_matrix:Array<Array<TextView>>, valuesMatrix : Array<IntArray>, context:Context)
        {
            for (i in 0 until buttons_matrix.size) {
                for (j in 0 until buttons_matrix[0].size) {
                    buttons_matrix[i][j].setBackgroundResource(R.drawable.tile_border)
                    buttons_matrix[i][j].text = '0'.toString()
                    buttons_matrix[i][j].setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    valuesMatrix[i][j] = 0
//                    buttons_matrix[i][j].setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
                }
            }
        }


        private fun generateNewElement(values_matrix:Array<IntArray>,
                                       buttons_matrix:Array<Array<TextView>>, context:Context) {
            var availableElements  = listOf<Int>()
            for (i in 0 until values_matrix.size) {
                for (j in 0 until values_matrix[0].size) {
                    if (0 == values_matrix[i][j]) {
                        availableElements  += i * 4 + j
                    }
                }
            }
            if (availableElements .isNotEmpty()) {
                Log.warning("Available: ")
                Log.warning(availableElements .toString())

                val randomIndex = Random().nextInt(availableElements .size)

                Log.warning("Random: ")
                Log.warning(availableElements [randomIndex].toString())
                Log.warning("___________________________________")

                when (availableElements [randomIndex]) {
                    0 -> {
                        generateNewElementWrite(buttons_matrix[0][0], this)
                        values_matrix[0][0] = 2
                    }
                    1 -> {
                        generateNewElementWrite(buttons_matrix[0][1], this)
                        values_matrix[0][1] = 2
                    }
                    2 -> {
                        generateNewElementWrite(buttons_matrix[0][2], this)
                        values_matrix[0][2] = 2
                    }
                    3 -> {
                        generateNewElementWrite(buttons_matrix[0][3], this)
                        values_matrix[0][3] = 2
                    }
                    4 -> {
                        generateNewElementWrite(buttons_matrix[1][0], this)
                        values_matrix[1][0] = 2
                    }
                    5 -> {
                        generateNewElementWrite(buttons_matrix[1][1], this)
                        values_matrix[1][1] = 2
                    }
                    6 -> {
                        generateNewElementWrite(buttons_matrix[1][2], this)
                        values_matrix[1][2] = 2
                    }
                    7 -> {
                        generateNewElementWrite(buttons_matrix[1][3], this)
                        values_matrix[1][3] = 2
                    }
                    8 -> {
                        generateNewElementWrite(buttons_matrix[2][0], this)
                        values_matrix[2][0] = 2
                    }
                    9 -> {
                        generateNewElementWrite(buttons_matrix[2][1], this)
                        values_matrix[2][1] = 2
                    }
                    10 -> {
                        generateNewElementWrite(buttons_matrix[2][2], this)
                        values_matrix[2][2] = 2
                    }
                    11 -> {
                        generateNewElementWrite(buttons_matrix[2][3], this)
                        values_matrix[2][3] = 2
                    }
                    12 -> {
                        generateNewElementWrite(buttons_matrix[3][0], this)
                        values_matrix[3][0] = 2
                    }
                    13 -> {
                        generateNewElementWrite(buttons_matrix[3][1], this)
                        values_matrix[3][1] = 2
                    }
                    14 -> {
                        generateNewElementWrite(buttons_matrix[3][2], this)
                        values_matrix[3][2] = 2
                    }
                    15 -> {
                        generateNewElementWrite(buttons_matrix[3][3], this)
                        values_matrix[3][3] = 2
                    }
                }
            }
        }

        private fun generateNewElementWrite(view: TextView, context: Context)
        {
            view.text = '2'.toString()
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
        }

    }


