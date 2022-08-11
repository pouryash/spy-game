package com.pourya.spy_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import com.mcdev.quantitizerlibrary.AnimationStyle
import com.mcdev.quantitizerlibrary.QuantitizerListener
import com.pourya.spy_game.databinding.ActivityConfigBinding
import com.pourya.spy_game.databinding.ActivityMainBinding
import com.pourya.spy_game.util.Constants
import com.pourya.spy_game.util.SharedPreferenceManager

class ConfigActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfigBinding
    lateinit var title: String
    lateinit var icon: Number
    lateinit var keyValue: String
    lateinit var sharedPreferenceManager: SharedPreferenceManager
    lateinit var numValue: Number

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setupViews()
    }

    private fun init() {
        sharedPreferenceManager = SharedPreferenceManager(this)
        title = intent.getStringExtra("title").toString()
        icon = intent.getIntExtra("icon", 0)
        keyValue = intent.getStringExtra("keyValue").toString()
        numValue = sharedPreferenceManager.getInteger(keyValue)
    }

    private fun setupViews() {
        binding.imgConfig.setImageDrawable(AppCompatResources.getDrawable(this, icon.toInt()))
        binding.txtTitleConfig.text = title

        binding.numberPickerConfig.apply {
            isReadOnly = true
            buttonAnimationEnabled = false
            setValueTextColor(R.color.main)
            textAnimationStyle = AnimationStyle.SLIDE_IN
            animationDuration = 200
            value = numValue.toInt()
        }

        binding.numberPickerConfig.setQuantitizerListener(object : QuantitizerListener {
            override fun onDecrease() {
            }

            override fun onIncrease() {
                if (keyValue == Constants.TIMERS_NUM_VALUE && numValue.toInt() == Constants.TIMERS_MAX_VALUE)
                    binding.numberPickerConfig.value = Constants.TIMERS_MAX_VALUE
            }

            override fun onValueChanged(value: Int) {
                if (keyValue == Constants.TIMERS_NUM_VALUE && value > Constants.TIMERS_MAX_VALUE)
                    return
                numValue = value
            }
        })

        binding.btnConfirmGameConfig.setOnClickListener {
            sharedPreferenceManager.saveInteger(keyValue, numValue.toInt())
            this.finish()
        }

    }
}