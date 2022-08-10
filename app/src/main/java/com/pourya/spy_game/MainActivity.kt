package com.pourya.spy_game

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.mcdev.quantitizerlibrary.AnimationStyle
import com.pourya.spy_game.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViews()

    }

    private fun setUpViews() {
        binding.numberPickerPlayer.apply {
            isReadOnly = true
            buttonAnimationEnabled = false
            setValueTextColor(R.color.main)
            setMinusIcon(R.drawable.ic_baseline_arrow_back_ios_24)
            setMinusIconBackgroundColor(android.R.color.transparent)
            setPlusIcon(R.drawable.ic_baseline_arrow_forward_ios_24)
            setPlusIconBackgroundColor(android.R.color.transparent)
            textAnimationStyle = AnimationStyle.SLIDE_IN
            animationDuration = 200
        }

        binding.numberPickerSpies.apply {
            isReadOnly = true
            buttonAnimationEnabled = false
            setValueTextColor(R.color.main)
            setMinusIcon(R.drawable.ic_baseline_arrow_back_ios_24)
            setMinusIconBackgroundColor(android.R.color.transparent)
            setPlusIcon(R.drawable.ic_baseline_arrow_forward_ios_24)
            setPlusIconBackgroundColor(android.R.color.transparent)
            textAnimationStyle = AnimationStyle.SLIDE_IN
            animationDuration = 200
        }
    }
}