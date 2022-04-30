package com.android.capstone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.android.capstone.R
import com.android.capstone.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    lateinit var mainBinding: ActivitySplashScreenBinding
    lateinit var topAnimation: Animation
    lateinit var bottomAnimation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        mainBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)


        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        mainBinding.text.animation = topAnimation
        mainBinding.welcome.animation = bottomAnimation
        mainBinding.slogan.animation = bottomAnimation

        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 4000)
    }
}