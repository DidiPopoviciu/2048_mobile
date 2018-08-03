package com.di.a2048

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.view.*
import android.widget.Toast
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

        var countVitality = vitalityBadge2.text.toString().toInt()

        var resetVitality = View.OnTouchListener(function = { view, motionEvent -> if(motionEvent.action == MotionEvent.ACTION_UP) {
            countVitality = 0
            openDialog.background = ContextCompat.getDrawable(this, R.drawable.ic_ic_endy_exhausted)
            vitalityBadge2.text = "0"
            addVitality.background = ContextCompat.getDrawable(this, R.drawable.ic_ic_add_vitality)
        }
            true })

        textV.setOnTouchListener(resetVitality)

        var listenerAddVitality = View.OnTouchListener(function = { view, motionEvent -> if(motionEvent.action == MotionEvent.ACTION_UP) {

            when (countVitality) {
                in 0..3 -> {
                    countVitality += 1
                    vitalityBadge2.text = countVitality.toString()


                    if (endyMessage.visibility == View.VISIBLE){
                        endyMessage.text = "+1 vitality"
                        Handler().postDelayed({
                            endyMessage.visibility = View.INVISIBLE
                        }, 1000)
                    } else {
                        endyMessage.visibility = View.VISIBLE
                        endyMessage.text = "+1 vitality"
                        Handler().postDelayed({
                            endyMessage.visibility = View.INVISIBLE
                        }, 1000)
                    }
                    openDialog.background = ContextCompat.getDrawable(this, R.drawable.ic_ic_endy_first_step)
                    }
                in 4..9 -> {
                    countVitality += 1
                    vitalityBadge2.text = countVitality.toString()
                    openDialog.background = ContextCompat.getDrawable(this, R.drawable.ic_ic_endy_second_step)



                    if (countVitality == 10)
                    {
//                        Toast.makeText(this, "Case 5-9, countVit=10", Toast.LENGTH_SHORT).show();
                        endyMessage.text = "Endy has all the energy in the world!"
                        if (endyMessage.visibility == View.INVISIBLE){
                            endyMessage.visibility = View.VISIBLE
                            Handler().postDelayed({
                                endyMessage.visibility = View.INVISIBLE
                            }, 5000)
                        }else {endyMessage.visibility = View.INVISIBLE}
                        openDialog.background = ContextCompat.getDrawable(this, R.drawable.ic_ic_endy_max)
                        addVitality.background = ContextCompat.getDrawable(this, R.drawable.ic_add_vitality_inactive)

                        Handler().postDelayed({
                            endyMessage.visibility = View.INVISIBLE
                        }, 5000)
                    } else {
                        if (endyMessage.visibility == View.VISIBLE){
                            endyMessage.text = "+1 vitality"
                            Handler().postDelayed({
                                endyMessage.visibility = View.INVISIBLE
                            }, 1000)
                        } else {
                            endyMessage.visibility = View.VISIBLE
                            endyMessage.text = "+1 vitality"
                            Handler().postDelayed({
                                endyMessage.visibility = View.INVISIBLE
                            }, 1000)
                        }
                    }
                }

                else -> {
//                    Toast.makeText(this, "In the else case", Toast.LENGTH_SHORT).show();
                    endyMessage.text = "Endy cannot store more vitality"
                    if (endyMessage.visibility == View.INVISIBLE){
                        endyMessage.visibility = View.VISIBLE
                        Handler().postDelayed({
                            endyMessage.visibility = View.INVISIBLE
                        }, 5000)
                    }

                }
            }

        }
            true
        })

        addVitality.setOnTouchListener(listenerAddVitality)

    }
}
