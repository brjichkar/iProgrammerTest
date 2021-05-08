package com.iprogrammertest.ui_section.splash_section

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iprogrammertest.ui_section.city_weather_section.MainActivity

class ActivitySplash : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        val mainActIntent = Intent(applicationContext, MainActivity::class.java)
        finish()
        startActivity(mainActIntent)
    }
}