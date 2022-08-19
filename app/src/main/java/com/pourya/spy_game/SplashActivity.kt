package com.pourya.spy_game

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jaredrummler.android.widget.AnimatedSvgView
import com.pourya.spy_game.databinding.ActivitySplashBinding
import com.pourya.spy_game.view.MainActivity


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val svgView = binding.animatedSvgView
        svgView.setFillColor(resources.getColor(R.color.secondary))
        svgView.setTraceColor(resources.getColor(R.color.secondary))
        svgView.setTraceResidueColor(resources.getColor(R.color.secondary))
        svgView.setViewportSize(512f, 512f)
        svgView.rebuildGlyphData()
        svgView.start()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 3500)

    }
}