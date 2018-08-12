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
import android.widget.GridView
import kotlinx.android.synthetic.main.custom_grid_tiles.view.*


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

    var textViewsMatrix = arrayOf<Array<SquareTextView>>()
    var movePerformed = 0

    var con: Context = this
    var img: IntArray = intArrayOf(
            R.drawable.ic_ic_face_grey, R.drawable.ic_ic_face_grey,
            R.drawable.ic_ic_face_grey, R.drawable.ic_ic_face_grey,
            R.drawable.ic_ic_face_grey, R.drawable.ic_ic_face_grey,
            R.drawable.ic_ic_face_grey, R.drawable.ic_ic_face_grey,
            R.drawable.ic_ic_face_grey, R.drawable.ic_ic_face_grey,
            R.drawable.ic_ic_face_grey, R.drawable.ic_ic_face_grey,
            R.drawable.ic_ic_face_grey, R.drawable.ic_ic_face_grey,
            R.drawable.ic_ic_face_grey, R.drawable.ic_ic_face_grey)

    var activeBackground: IntArray = intArrayOf(
            R.drawable.tile_background, R.drawable.tile_background,
            R.drawable.tile_background, R.drawable.tile_background,
            R.drawable.tile_background, R.drawable.tile_background,
            R.drawable.tile_background, R.drawable.tile_background,
            R.drawable.tile_background, R.drawable.tile_background,
            R.drawable.tile_background, R.drawable.tile_background,
            R.drawable.tile_background, R.drawable.tile_background,
            R.drawable.tile_background, R.drawable.tile_background)
    var name: Array<String> = arrayOf(  "0", "0", "0", "0",
                                        "0", "0", "0", "0",
                                        "0", "0", "0", "0",
                                        "0", "0", "0", "0")
    lateinit var gv: GridView
    lateinit var cl: CustomAdapter

    var globalEndyVitality = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_game)
        supportActionBar?.hide()

        gv = findViewById<GridView>(R.id.tiles_background)
        cl = CustomAdapter(img, con, name, activeBackground)
        gv.adapter = cl

        val mypreference = MyPreference(this)
        var gameVitality = mypreference.getVitalityCount(PREFERENCE_VITALITY_COUNT)

        globalEndyVitality = gameVitality

        checkEndyVitality()

        vitalityInfo.text = gameVitality.toString()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        textViewsMatrix = initTextViewMatrix()
        startNewGame()

        var listener = View.OnTouchListener(function = { view, motionEvent ->

            if (gameVitality > 0) {

                if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                    view.y = motionEvent.rawY - view.height
                    view.x = motionEvent.rawX - view.width / 2

                    endyPowerUp.background = ContextCompat.getDrawable(this, R.drawable.ic_endy_drag)

                    endyCollided()
                }
                if (motionEvent.action == MotionEvent.ACTION_UP) {

                    if (endyCollided() == false) {
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
                    }
                    else {
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

                            if (gameVitality == 0)
                                endyPowerUp.background = ContextCompat.getDrawable(this, R.drawable.ic_endy_exhausted_background)

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
                        }
                        else {
                            gameVitality = 0

                            endyPowerUp.background = ContextCompat.getDrawable(this, R.drawable.ic_endy_exhausted_background)

                            mypreference.setVItalityCount(gameVitality)
                        }
                    }
                }
            }
            else {
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

        gv.setOnTouchListener(object: OnSwipeTouchListener(this) {

            override fun onSwipeLeft() {
                var valuesMatrix = arrayOf(intArrayOf(cl.name[0].toInt(), cl.name[1].toInt(), cl.name[2].toInt(), cl.name[3].toInt()),
                        intArrayOf(cl.name[4].toInt(), cl.name[5].toInt(), cl.name[6].toInt(), cl.name[7].toInt()),
                        intArrayOf(cl.name[8].toInt(), cl.name[9].toInt(), cl.name[10].toInt(), cl.name[11].toInt()),
                        intArrayOf(cl.name[12].toInt(), cl.name[13].toInt(), cl.name[14].toInt(), cl.name[15].toInt())
                )
                swipeLeft(valuesMatrix)
            }
            override fun onSwipeRight() {
                var valuesMatrix = arrayOf(intArrayOf(cl.name[0].toInt(), cl.name[1].toInt(), cl.name[2].toInt(), cl.name[3].toInt()),
                        intArrayOf(cl.name[4].toInt(), cl.name[5].toInt(), cl.name[6].toInt(), cl.name[7].toInt()),
                        intArrayOf(cl.name[8].toInt(), cl.name[9].toInt(), cl.name[10].toInt(), cl.name[11].toInt()),
                        intArrayOf(cl.name[12].toInt(), cl.name[13].toInt(), cl.name[14].toInt(), cl.name[15].toInt())
                )
                swipeRight(valuesMatrix)
            }
            override fun onSwipeUp() {
                var valuesMatrix = arrayOf(intArrayOf(cl.name[0].toInt(), cl.name[1].toInt(), cl.name[2].toInt(), cl.name[3].toInt()),
                        intArrayOf(cl.name[4].toInt(), cl.name[5].toInt(), cl.name[6].toInt(), cl.name[7].toInt()),
                        intArrayOf(cl.name[8].toInt(), cl.name[9].toInt(), cl.name[10].toInt(), cl.name[11].toInt()),
                        intArrayOf(cl.name[12].toInt(), cl.name[13].toInt(), cl.name[14].toInt(), cl.name[15].toInt())
                )
                swipeUp(valuesMatrix)
            }
            override fun onSwipeDown() {
                var valuesMatrix = arrayOf(intArrayOf(cl.name[0].toInt(), cl.name[1].toInt(), cl.name[2].toInt(), cl.name[3].toInt()),
                        intArrayOf(cl.name[4].toInt(), cl.name[5].toInt(), cl.name[6].toInt(), cl.name[7].toInt()),
                        intArrayOf(cl.name[8].toInt(), cl.name[9].toInt(), cl.name[10].toInt(), cl.name[11].toInt()),
                        intArrayOf(cl.name[12].toInt(), cl.name[13].toInt(), cl.name[14].toInt(), cl.name[15].toInt())
                )
                swipeDown(valuesMatrix)
            }
        }
        )

        closeGame.setOnClickListener {
            super.finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    private fun initTextViewMatrix() : Array<Array<SquareTextView>>{

        return arrayOf( arrayOf(getTextViewByNumber(0), getTextViewByNumber(1),getTextViewByNumber(2), getTextViewByNumber(3)),
                        arrayOf(getTextViewByNumber(4), getTextViewByNumber(5),getTextViewByNumber(6), getTextViewByNumber(7)),
                        arrayOf(getTextViewByNumber(8), getTextViewByNumber(9),getTextViewByNumber(10), getTextViewByNumber(11)),
                        arrayOf(getTextViewByNumber(12), getTextViewByNumber(13),getTextViewByNumber(14), getTextViewByNumber(15))
        )
    }

    private fun generateNewElement() {
        Log.warning("cl: ")
        Log.warning(cl.name.toString())
        var availableElements  = listOf<Int>()
        for (i in 0 until cl.name.size) {
            if (0 == cl.name[i].toInt()) {
                availableElements  += i
            }
        }

        if (availableElements .isNotEmpty()) {
            Log.warning("Available: ")
            Log.warning(availableElements .toString())

            val randomIndex = Random().nextInt(availableElements .size)

            Log.warning("Random: ")
            Log.warning(availableElements[randomIndex].toString())

            cl.name[availableElements[randomIndex]] = "2"
            cl.activeBackground[availableElements[randomIndex]] = R.drawable.active_tile_background
            cl.img[availableElements[randomIndex]] = R.drawable.ic_tile_faces_01

            cl.notifyDataSetChanged()
            gv.adapter = cl

        }
    }

    private fun generateNewElementWrite(view: TextView, context: Context)
    {
        view.text = '2'.toString()
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
    }

    private fun getTextViewByNumber(n: Int): SquareTextView {
        return cl.getView(n, tiles_background, null).element_skin.tile_number_background
    }

    private fun getTextViewValueByNumber(n: Int): Int{
        return (cl.getView(1, tiles_background, null).element_skin.tile_number_background.text).toString().toInt()
    }
    private fun startNewGame(){
        generateNewElement()
        generateNewElement()
    }

    fun endyCollided(): Boolean {

        val viewRect: Rect = Rect(tiles_background.x.toInt(), tiles_background.y.toInt(), tiles_background.x.toInt() + tiles_background.width, tiles_background.y.toInt() + tiles_background.height)
        val endyRect: Rect = Rect(endyPowerUp.x.toInt(), endyPowerUp.y.toInt(), endyPowerUp.x.toInt() + endyPowerUp.width, endyPowerUp.y.toInt() + endyPowerUp.height)
        return viewRect.intersect(endyRect)

    }

    private fun swipeUp(valuesMatrix : Array<IntArray>){
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
                cl.name[j * 4 + i] = currentRow[j].toString()
            }
            updateGrid()
        }
        if (movePerformed > 0) {
            generateNewElement()
        }
    }

    private fun swipeDown(valuesMatrix : Array<IntArray>){
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
                cl.name[j * 4 + i] = currentRow[j].toString()
            }
            updateGrid()
        }
        if (movePerformed > 0) {
            generateNewElement()
        }
    }

    private fun swipeRight(valuesMatrix : Array<IntArray>){

        var currentRow = intArrayOf(0,0,0,0)
        for (i in 0 until valuesMatrix.size) {

            Log.info("row number " + i.toString())
            currentRow = valuesMatrix[i]
            currentRow.reverse()
            currentRow = slide(currentRow)
            currentRow = combine(currentRow)
            currentRow = slide(currentRow)
            currentRow.reverse()
            for(j in 0 until valuesMatrix[0].size) {
                cl.name[i * 4 + j] = currentRow[j].toString()
            }
            updateGrid()
        }
        if (movePerformed > 0) {
            generateNewElement()
        }
    }

    private fun swipeLeft(valuesMatrix : Array<IntArray>){
        movePerformed = 0

        var currentRow = intArrayOf(0,0,0,0)

        for (i in 0 until valuesMatrix.size) {

            Log.info("row number " + i.toString())
            currentRow = valuesMatrix[i]
            currentRow = slide(currentRow)
            currentRow = combine(currentRow)
            currentRow = slide(currentRow)
            for(j in 0 until valuesMatrix[0].size) {
                cl.name[i * 4 + j] = currentRow[j].toString()
            }
            updateGrid()
        }
        if (movePerformed > 0) {
            generateNewElement()
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

    fun checkEndyVitality() {
        if (globalEndyVitality == 0)
            endyPowerUp.background = ContextCompat.getDrawable(this, R.drawable.ic_ic_endy_exhausted)
        else
            endyPowerUp.background = ContextCompat.getDrawable(this, R.drawable.ic_endy_normal)
    }

    fun updateGrid(){
        for (j in 0 until (cl.name.size)) {
            when (cl.name[j].toInt()) {
                0 -> {
                    cl.activeBackground[j] = R.drawable.tile_background
                    cl.img[j] = R.drawable.ic_ic_face_grey
                }
                2 -> {
                    cl.img[j] = R.drawable.ic_tile_faces_01
                    cl.activeBackground[j] = R.drawable.active_tile_background
                }
                4 -> {
                    cl.img[j] = R.drawable.ic_tile_faces_02
                    cl.activeBackground[j] = R.drawable.active_tile_background
                }
                8 -> {
                    cl.img[j] = R.drawable.ic_tile_faces_03
                    cl.activeBackground[j] = R.drawable.active_tile_background
                }
                16 -> {
                    cl.img[j] = R.drawable.ic_tile_faces_04
                    cl.activeBackground[j] = R.drawable.active_tile_background
                }
                32-> {
                    cl.img[j] = R.drawable.ic_tile_faces_05
                    cl.activeBackground[j] = R.drawable.active_tile_background
                }
                64 -> {
                    cl.img[j] = R.drawable.ic_tile_faces_06
                    cl.activeBackground[j] = R.drawable.active_tile_background
                }
                128 -> {
                    cl.img[j] = R.drawable.ic_tile_faces_07
                    cl.activeBackground[j] = R.drawable.active_tile_background
                }
                256 -> {
                    cl.img[j] = R.drawable.ic_tile_faces_08
                    cl.activeBackground[j] = R.drawable.active_tile_background
                }
                512 -> {
                    cl.img[j] = R.drawable.ic_tile_faces_09
                    cl.activeBackground[j] = R.drawable.active_tile_background
                }
                1024 -> {
                    cl.img[j] = R.drawable.ic_tile_faces_10
                    cl.activeBackground[j] = R.drawable.active_tile_background
                }
                2048 -> {
                    cl.img[j] = R.drawable.ic_tile_faces_11
                    cl.activeBackground[j] = R.drawable.active_tile_background
                }
                4096 -> {
                    cl.img[j] = R.drawable.ic_tile_faces_12
                    cl.activeBackground[j] = R.drawable.active_tile_background
                }
                8192 -> {
                    cl.img[j] = R.drawable.ic_tile_faces_13
                    cl.activeBackground[j] = R.drawable.active_tile_background
                }
                16384 -> {
                    cl.img[j] = R.drawable.ic_tile_faces_14
                    cl.activeBackground[j] = R.drawable.active_tile_background
                }
                32768 -> {
                    cl.img[j] = R.drawable.ic_tile_faces_15
                    cl.activeBackground[j] = R.drawable.active_tile_background
                }
                65536 -> {
                    cl.img[j] = R.drawable.ic_tile_faces_16
                    cl.activeBackground[j] = R.drawable.active_tile_background
                }
                131072 -> {
                    cl.img[j] = R.drawable.ic_tile_faces_17
                    cl.activeBackground[j] = R.drawable.active_tile_background
                }
            }
            cl.notifyDataSetChanged()
            gv.adapter = cl
        }
    }
}


