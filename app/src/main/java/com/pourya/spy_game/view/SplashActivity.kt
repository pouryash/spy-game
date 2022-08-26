package com.pourya.spy_game.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.pourya.spy_game.BuildConfig

import com.pourya.spy_game.R
import com.pourya.spy_game.databinding.ActivitySplashBinding
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusInitListener
import ir.tapsell.plus.model.AdNetworkError
import ir.tapsell.plus.model.AdNetworks


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTapsell()

        val svgView = binding.animatedSvgView
        svgView.setFillColor(resources.getColor(R.color.secondary))
        svgView.setTraceColor(resources.getColor(R.color.secondary))
        svgView.setTraceResidueColor(resources.getColor(R.color.secondary))
        svgView.setViewportSize(512f, 512f)
        svgView.rebuildGlyphData()
        svgView.start()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3500)

    }

    /**
     * this method initialize tapsell
     */
    private fun initTapsell() {
        TapsellPlus.setDebugMode(Log.DEBUG)
        TapsellPlus.initialize(this, BuildConfig.TAPSELL_KEY,
            object : TapsellPlusInitListener {
                override fun onInitializeSuccess(adNetworks: AdNetworks) {
                    Log.d("onInitializeSuccess", adNetworks.name)
                }

                override fun onInitializeFailed(
                    adNetworks: AdNetworks,
                    adNetworkError: AdNetworkError
                ) {
                    Log.e(
                        "onInitializeFailed",
                        "ad network: " + adNetworks.name + ", error: " + adNetworkError.errorMessage
                    )
                }
            })
    }
}