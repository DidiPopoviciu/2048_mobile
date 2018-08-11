package com.di.a2048

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.activity_game.*
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import java.util.*
import java.util.logging.Logger
import android.view.animation.DecelerateInterpolator
import android.util.DisplayMetrics


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
    var movePerformed = 0





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_game)
        supportActionBar?.hide()

        val mypreference = MyPreference(this)
        var gameVitality = mypreference.getVitalityCount(PREFERENCE_VITALITY_COUNT)

//            var endyVitality = gameVitality

        vitalityInfo.text = gameVitality.toString()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var widthScreen = displayMetrics.widthPixels
        var heightScreen = displayMetrics.heightPixels

//        textViewsMatrix = arrayOf(arrayOf(element1, element2, element3, element4),
//                arrayOf(element5, element6, element7, element8),
//                arrayOf(element9, element10, element11, element12),
//                arrayOf(element13, element14, element15, element16)
//        )

        startNewGame()


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
                    swipeLeft(textViewsMatrix, valuesMatrix)
                }

                // if UP to Down sweep event on screen
                if (y1 < y2 && y2 - y1 > minDistance) {
                    swipeDown(textViewsMatrix, valuesMatrix) }

                //if Down to UP sweep event on screen
                if (y1 > y2 && y1 - y2 > minDistance) {
                    swipeUp(textViewsMatrix, valuesMatrix)
                }

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

    private fun swipeUp(buttonsMatrix:Array<Array<TextView>>, valuesMatrix : Array<IntArray>){
        movePerformed = 0
        var currentRow = intArrayOf(0,0,0,0)
        for (i in 0 until valuesMatrix.size) {

            for (j in 0 until valuesMatrix.size) {
                currentRow[j] = valuesMatrix[j][i]
            }

            Log.info("row number " + i.toString())
            currentRow = slide(currentRow)
            currentRow = combine(currentRow)
            currentRow = slide(currentRow)
            Log.info("pass")
            //reverse
            for (j in 0 until valuesMatrix.size) {
                valuesMatrix[j][i] = currentRow[j]
            }
        }
        if (movePerformed > 0) {
            updateView(buttonsMatrix, valuesMatrix, this)
            generateNewElement(valuesMatrix, textViewsMatrix, this)
        }
    }

    private fun swipeDown(buttonsMatrix:Array<Array<TextView>>, valuesMatrix : Array<IntArray>){
        movePerformed = 0
        var currentRow = intArrayOf(0,0,0,0)
        for (i in 0 until valuesMatrix.size) {

            for (j in 0 until valuesMatrix.size) {
                currentRow[j] = valuesMatrix[j][i]
            }

            Log.info("row number " + i.toString())
            currentRow.reverse()
            currentRow = slide(currentRow)
            currentRow = combine(currentRow)
            currentRow = slide(currentRow)
            currentRow.reverse()
            Log.info("pass")
            //reverse
            for (j in 0 until valuesMatrix.size) {
                valuesMatrix[j][i] = currentRow[j]
            }
        }
        if (movePerformed > 0) {
            updateView(buttonsMatrix, valuesMatrix, this)
            generateNewElement(valuesMatrix, textViewsMatrix, this)
        }
    }

    private fun swipeRight(buttonsMatrix:Array<Array<TextView>>, valuesMatrix : Array<IntArray>)
    {
        movePerformed = 0
        var elementMap = arrayOf(arrayOf("el0", "el1", "el2", "el3"),
                arrayOf("el4", "el5", "el6", "el7"),
                arrayOf("el8", "el9", "el10", "el11"),
                arrayOf("el12", "el13", "el14", "el15")
        )
        var currentRow = intArrayOf(0,0,0,0)
        for (i in 0 until valuesMatrix.size) {

            Log.info("row number " + i.toString())
            currentRow = valuesMatrix[i]
            currentRow.reverse()
            currentRow = slide(currentRow)
            currentRow = combine(currentRow)
            currentRow = slide(currentRow)
            currentRow.reverse()
            valuesMatrix[i] = currentRow
        }
        if (movePerformed > 0) {
            updateView(buttonsMatrix, valuesMatrix, this)
            generateNewElement(valuesMatrix, textViewsMatrix, this)
        }
    }

    private fun swipeLeft(buttonsMatrix:Array<Array<TextView>>, valuesMatrix : Array<IntArray>)
    {
        movePerformed = 0
        var elementMap = arrayOf(arrayOf("el0", "el1", "el2", "el3"),
                arrayOf("el4", "el5", "el6", "el7"),
                arrayOf("el8", "el9", "el10", "el11"),
                arrayOf("el12", "el13", "el14", "el15")
        )
        var currentRow = intArrayOf(0,0,0,0)

        for (i in 0 until valuesMatrix.size) {

            Log.info("row number " + i.toString())
            currentRow = valuesMatrix[i]
            currentRow = slide(currentRow)
            currentRow = combine(currentRow)
            currentRow = slide(currentRow)
            valuesMatrix[i] = currentRow
        }
        if (movePerformed > 0) {
            updateView(buttonsMatrix, valuesMatrix, this)
            generateNewElement(valuesMatrix, textViewsMatrix, this)
        }
    }

    private fun slide(row: IntArray): IntArray{
        var numberOfElements = 0
        for (element in row) {
            if (element != 0) {
                numberOfElements +=1
            }

        }
        Log.info("Number of elements " + numberOfElements.toString())
        if (numberOfElements != 0){
//                    row is not empty
            for(j in 0 until (numberOfElements))
            {
                var moved = 0
                while (row[j] == 0){
                    for(k in j until row.size-1) {
                        movePerformed += 1
                        row[k] = row[k + 1]
                        moved = 1
                    }
                    if (moved == 1) {
                        row[row.size - 1] = 0
                    }
                }
            }
        }
        return row
    }

    private fun combine(row: IntArray): IntArray{
        var numberOfElements = 0

        for (element in row) {
            if (element != 0) {
                numberOfElements +=1
            }

        }
        Log.info("Number of elements " + numberOfElements.toString())
        if (numberOfElements >= 2){
//                    row is not empty
            for(j in 0 until (row.size-1))
            {
                if (row[j] == row[j+1]) {
                    movePerformed += 1
                    row[j] = row[j] + row[j+1]
                    row[j+1] = 0
                }

            }
        }
        return row
    }

    private fun updateView(buttonsMatrix:Array<Array<TextView>>, valuesMatrix : Array<IntArray>, context:Context)
    {
        for(i in valuesMatrix.size-1 downTo  0){
            for(j in valuesMatrix[i].size-1 downTo 0){
                if (valuesMatrix[i][j] != buttonsMatrix[i][j].text.toString().toInt()){
                    if (buttonsMatrix[i][j].text.toString().toInt() == 0){ // and valuesMatrix[i][j] != 0){
                        buttonsMatrix[i][j].setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
                    }
                    else {
                        if (valuesMatrix[i][j] == 0)
                        {
                            buttonsMatrix[i][j].setBackgroundResource(R.drawable.tile_border)
                        }
                    }
                }
                buttonsMatrix[i][j].text = valuesMatrix[i][j].toString()
                print("update")
            }
        }
    }

    private fun swipeRightOld(buttonsMatrix:Array<Array<TextView>>, valuesMatrix : Array<IntArray>){
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


