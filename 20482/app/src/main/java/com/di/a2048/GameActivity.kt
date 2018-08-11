package com.di.a2048

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game.*
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.logging.Logger
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.GridLayout
import com.di.a2048.R.id.message
import android.util.DisplayMetrics
import android.widget.GridView


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
        var name: Array<String> = arrayOf("0", "2", "0", "4", "0", "0", "0", "0", "8", "8", "8", "16", "0", "0", "32", "0")
        lateinit var gv: GridView
        lateinit var cl: CustomAdapter



        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)


            window.requestFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
            setContentView(R.layout.activity_game)
            supportActionBar?.hide()

            gv = findViewById(R.id.tiles_background) as GridView
            cl = CustomAdapter(img, con, name)
            gv.adapter = cl

            val mypreference = MyPreference(this)
            var gameVitality = mypreference.getVitalityCount(PREFERENCE_VITALITY_COUNT)

//            var endyVitality = gameVitality

            vitalityInfo.text = gameVitality.toString()

            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)

            var widthScreen = displayMetrics.widthPixels
            var heightScreen = displayMetrics.heightPixels

/*            textViewsMatrix = arrayOf(arrayOf(element1, element2, element3, element4),
                    arrayOf(element5, element6, element7, element8),
                    arrayOf(element9, element10, element11, element12),
                    arrayOf(element13, element14, element15, element16)
            )*/

//            startNewGame()


                var listener = View.OnTouchListener(function = { view, motionEvent ->

                    if (gameVitality > 0) {

                        Log.warning("if 1")

                        if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                            view.y = motionEvent.rawY - view.height
                            view.x = motionEvent.rawX - view.width / 2

                            endyPowerUp.background = ContextCompat.getDrawable(this, R.drawable.ic_endy_drag)

                            endyCollided()

                        }

                        if (motionEvent.action == MotionEvent.ACTION_UP) {

                            if (endyCollided() == false) {
                                //                        Toast.makeText(this, "if: Not Collided", Toast.LENGTH_SHORT).show()

                                val animationEndyX = ObjectAnimator.ofFloat(endyPowerUp, "translationX", 0f)
                                animationEndyX.duration = 350
                                animationEndyX.setInterpolator(AccelerateDecelerateInterpolator())
                                animationEndyX.start()

                                val animationEndyY = ObjectAnimator.ofFloat(endyPowerUp, "translationY", 0f)
                                animationEndyY.duration = 350
                                animationEndyY.setInterpolator(DecelerateInterpolator())
                                animationEndyY.start()

                                Handler().postDelayed({
                                    val headShake = ObjectAnimator.ofFloat(endyPowerUp, "translationX", 0f, 15f, -15f, 6f, -6f, 0f)
                                    headShake.duration = 500
                                    headShake.start()
                                }, 350)

                                endyPowerUp.background = ContextCompat.getDrawable(this, R.drawable.ic_endy_normal)
                            } else {
                                //                        Toast.makeText(this, "else: Collided", Toast.LENGTH_SHORT).show()
                                //                        endyVitality--

                                val animationEndyActivatedX = ObjectAnimator.ofFloat(endyPowerUp, "scaleX", 1.5f, 0f)
                                animationEndyActivatedX.duration = 250
                                animationEndyActivatedX.setInterpolator(AccelerateDecelerateInterpolator())
                                animationEndyActivatedX.start()

                                val animationEndyActivatedY = ObjectAnimator.ofFloat(endyPowerUp, "scaleY", 1.5f, 0f)
                                animationEndyActivatedY.duration = 250
                                animationEndyActivatedY.setInterpolator(AccelerateDecelerateInterpolator())
                                animationEndyActivatedY.start()

                                Handler().postDelayed({

                                    endyPowerUp.background = ContextCompat.getDrawable(this, R.drawable.ic_endy_normal)

                                    val animationEndyActivatedGoBackX = ObjectAnimator.ofFloat(endyPowerUp, "translationX", 0f)
                                    animationEndyActivatedGoBackX.duration = 0
                                    animationEndyActivatedGoBackX.start()

                                    val animationEndyActivatedGoBackY = ObjectAnimator.ofFloat(endyPowerUp, "translationY", 0f)
                                    animationEndyActivatedGoBackY.duration = 0
                                    animationEndyActivatedGoBackY.start()
                                }, 450)

                                Handler().postDelayed({
                                    val endyPopUpX = ObjectAnimator.ofFloat(endyPowerUp, "scaleX", 1.2f, 0.9f, 1f)
                                    endyPopUpX.duration = 200
                                    endyPopUpX.start()

                                    val endyPopUpY = ObjectAnimator.ofFloat(endyPowerUp, "scaleY", 1.2f, 0.9f, 1f)
                                    endyPopUpY.duration = 200
                                    endyPopUpY.start()
                                }, 500)

                                if (gameVitality > 0) {

                                    gameVitality -= 1

                                    vitalityInfo.text = gameVitality.toString()

                                    mypreference.setVItalityCount(gameVitality)
                                } else {
                                    gameVitality = 0

                                    //                            vitalityInfo.text = gameVitality.toString()

                                    mypreference.setVItalityCount(gameVitality)
                                }

                            }
                        }
                    }
                    else {

                        Log.warning("if 2")

                        val headShake = ObjectAnimator.ofFloat(endyPowerUp, "translationX", 0f, 15f, -15f, 6f, -6f, 0f)
                        headShake.duration = 500
                        headShake.start()
                    }

                    true
                })
                endyPowerUp.setOnTouchListener(listener)


            var returnEndy = View.OnTouchListener(function = { view, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP) {

                    startNewGame()

                }
                true
            })
            restartGame.setOnTouchListener(returnEndy)


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
                        swipeRight(textViewsMatrix, valuesMatrix)
                        generateNewElement(valuesMatrix, textViewsMatrix, this)


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
                        swipeDown(textViewsMatrix, valuesMatrix)
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

        private fun startNewGame(){
            initMatrix(textViewsMatrix, valuesMatrix, this)
            generateNewElement(valuesMatrix, textViewsMatrix, this)
            generateNewElement(valuesMatrix, textViewsMatrix, this)
        }

        fun endyCollided(): Boolean {

            val viewRect: Rect = Rect(tiles_background.x.toInt(), tiles_background.y.toInt(), tiles_background.x.toInt() + tiles_background.width, tiles_background.y.toInt() + tiles_background.height)
            val endyRect: Rect = Rect(endyPowerUp.x.toInt(), endyPowerUp.y.toInt(), endyPowerUp.x.toInt() + endyPowerUp.width, endyPowerUp.y.toInt() + endyPowerUp.height)
            return viewRect.intersect(endyRect)

        }

        private fun swipeDown(buttonsMatrix:Array<Array<TextView>>, valuesMatrix : Array<IntArray>){
            for (i in 0 until valuesMatrix[0].size) {
                for (j in valuesMatrix.size-1 until 0) {

                    if(valuesMatrix[i][j] == valuesMatrix[i][j-1]) {
                        Log.warning("transition:  x[{}][{}] -> x[{}][{}] - ->".format(i,j,i,j))
                    }
                }
            }
        }

        private fun swipeRight(buttonsMatrix:Array<Array<TextView>>, valuesMatrix : Array<IntArray>){
            Log.warning("Left to Right Swap Performed")

            var elementMap = arrayOf(arrayOf("el0", "el1", "el2", "el3"),
                   arrayOf("el4", "el5", "el6", "el7"),
                    arrayOf("el8", "el9", "el10", "el11"),
                    arrayOf("el12", "el13", "el14", "el15")
            )
            var currentRow = intArrayOf(0,0,0,0)
            for (i in 0 until valuesMatrix.size) {
                Log.info("row number " + i.toString())
                currentRow = valuesMatrix[i]
                for (k in 0..3) {
                    for (j in 0..2) {
                        if ((currentRow[j] != 0) and (currentRow[j+1] == 0)) {
                            Log.info("slide " + elementMap[i][j] + " with 1")
                            elementMap[i][j+1] = elementMap[i][j]
                            elementMap[i][j] = "x"
                            currentRow[j + 1] = currentRow[j]
                            currentRow[j] = 0
                            valuesMatrix[i][j+1] = valuesMatrix[i][j]
                            valuesMatrix[i][j] = 0
                        }
                    }
                }
            }
            Log.info("finish")
        }
    }


