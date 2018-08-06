package com.di.a2048

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
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

//                    val sharedPref = MainActivity.getPreferences(Context.MODE_PRIVATE) ?: return
//                    with (sharedPref.edit()) {
//                        putInt(getString(R.string.vitalityBadge2), newVitalityCount)
//                        commit()
//                    }

                    val headScaleY = ObjectAnimator.ofFloat(openDialog, "scaleY", 1.2f, 1f)
                    headScaleY.duration = 200
                    headScaleY.start()

                    val headScaleX = ObjectAnimator.ofFloat(openDialog, "scaleX", 1.2f, 1f)
                    headScaleX.duration = 200
                    headScaleX.start()

                    val badgeScaleY = ObjectAnimator.ofFloat(vitalityBadge2, "scaleY", 1.2f, 1f)
                    badgeScaleY.duration = 200
                    badgeScaleY.start()

                    val badgeScaleX = ObjectAnimator.ofFloat(vitalityBadge2, "scaleX", 1.2f, 1f)
                    badgeScaleX.duration = 200
                    badgeScaleX.start()

                    vitalityBadge2.text = countVitality.toString()


                    if (endyMessage.visibility == View.VISIBLE){
                        endyMessage.text = "+1 vitality"
                        Handler().postDelayed({
                            endyMessage.visibility = View.INVISIBLE
                        }, 500)
                    }
                    else {
                        endyMessage.visibility = View.VISIBLE
                        endyMessage.text = "+1 vitality"
                        Handler().postDelayed({
                            endyMessage.visibility = View.INVISIBLE
                        }, 500)
                    }
                    openDialog.background = ContextCompat.getDrawable(this, R.drawable.ic_ic_endy_first_step)
                    }
                in 4..8 -> {
                    countVitality += 1

                    val headScaleY = ObjectAnimator.ofFloat(openDialog, "scaleY", 1.2f, 1f)
                    headScaleY.duration = 200
                    headScaleY.start()

                    val headScaleX = ObjectAnimator.ofFloat(openDialog, "scaleX", 1.2f, 1f)
                    headScaleX.duration = 200
                    headScaleX.start()

                    val badgeScaleY = ObjectAnimator.ofFloat(vitalityBadge2, "scaleY", 1.2f, 1f)
                    badgeScaleY.duration = 200
                    badgeScaleY.start()

                    val badgeScaleX = ObjectAnimator.ofFloat(vitalityBadge2, "scaleX", 1.2f, 1f)
                    badgeScaleX.duration = 200
                    badgeScaleX.start()

                    vitalityBadge2.text = countVitality.toString()
                    openDialog.background = ContextCompat.getDrawable(this, R.drawable.ic_ic_endy_second_step)

                    if (endyMessage.visibility == View.VISIBLE) {
                        endyMessage.text = "+1 vitality"
                        Handler().postDelayed({
                            endyMessage.visibility = View.INVISIBLE
                        }, 500)
                    }
                    else {
                        endyMessage.visibility = View.VISIBLE
                        endyMessage.text = "+1 vitality"
                        Handler().postDelayed({
                            endyMessage.visibility = View.INVISIBLE
                        }, 500)
                    }
                }
                9 -> {
//                        Toast.makeText(this, "Case 5-9, countVit=10", Toast.LENGTH_SHORT).show();
                    countVitality += 1

                    val headShake = ObjectAnimator.ofFloat(openDialog, "scaleY", 1.2f, 1f)
                    headShake.duration = 200
                    headShake.start()

                    val headScaleX = ObjectAnimator.ofFloat(openDialog, "scaleX", 1.2f, 1f)
                    headScaleX.duration = 200
                    headScaleX.start()

                    val badgeScaleY = ObjectAnimator.ofFloat(vitalityBadge2, "scaleY", 1.2f, 1f)
                    badgeScaleY.duration = 200
                    badgeScaleY.start()

                    val badgeScaleX = ObjectAnimator.ofFloat(vitalityBadge2, "scaleX", 1.2f, 1f)
                    badgeScaleX.duration = 200
                    badgeScaleX.start()

                    vitalityBadge2.text = countVitality.toString()
                    endyMessage.text = "Endy has all the energy in the world!"
                    if (endyMessage.visibility == View.INVISIBLE){
                        endyMessage.visibility = View.VISIBLE
                        Handler().postDelayed({
                            endyMessage.visibility = View.INVISIBLE
                        }, 3000)
                    }
                    else {endyMessage.visibility = View.INVISIBLE}
                    openDialog.background = ContextCompat.getDrawable(this, R.drawable.ic_ic_endy_max)
                    addVitality.background = ContextCompat.getDrawable(this, R.drawable.ic_add_vitality_inactive)

                    Handler().postDelayed({
                        endyMessage.visibility = View.INVISIBLE
                    }, 2500)

                }
                else -> {
//                    Toast.makeText(this, "In the else case", Toast.LENGTH_SHORT).show();

                    val headShake = ObjectAnimator.ofFloat(openDialog, "translationX", 0f, 15f, -15f, 6f, -6f, 0f)
                    headShake.duration = 500
                    headShake.start()

                    openDialog.background = ContextCompat.getDrawable(this, R.drawable.ic_ic_endy_powerup)

                    Handler().postDelayed({
                        openDialog.background = ContextCompat.getDrawable(this, R.drawable.ic_ic_endy_happy)
                    }, 500)

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
