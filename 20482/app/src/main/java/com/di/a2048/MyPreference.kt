package com.di.a2048

import android.content.Context

val PREFERENCE_NAME = "SharedPreferenceExample"
val PREFERENCE_VITALITY_COUNT = "LoginCount"

class MyPreference(context : Context){


    val preference = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)

    fun getVitalityCount() : Int{
        return preference.getInt(PREFERENCE_VITALITY_COUNT,0)
    }

    fun getVitalityCount(KEY : String) : Int{
        return preference.getInt(KEY,0)
    }

    fun setVItalityCount(count:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_VITALITY_COUNT, count)
        editor.apply()
    }

}