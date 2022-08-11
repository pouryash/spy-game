package com.pourya.spy_game

import android.annotation.SuppressLint
import android.content.Intent
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
import com.mcdev.quantitizerlibrary.QuantitizerListener
import com.pourya.spy_game.databinding.ActivityMainBinding
import com.pourya.spy_game.util.Constants
import com.pourya.spy_game.util.SharedPreferenceManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
        updateValues()

    }

    private fun updateValues() {
        binding.txtPlayerNumValue.text =
            sharedPreferenceManager.getInteger(Constants.PLAYERS_NUM_VALUE).toString()
        binding.txtSpiesNumValue.text =
            sharedPreferenceManager.getInteger(Constants.SPIES_NUM_VALUE).toString()
        binding.txtTimerValue.text =
            sharedPreferenceManager.getInteger(Constants.TIMERS_NUM_VALUE).toString() + "m"
    }

    private fun setupViews() {
        sharedPreferenceManager = SharedPreferenceManager(this)

        updateValues()

        binding.btnStartGame.setOnClickListener {
            startActivity(Intent(this, ChooseRoleActivity::class.java))
        }

        binding.cardPlayerNum.setOnClickListener {
            var intent = Intent(this, ConfigActivity::class.java)
            intent.putExtra("title", resources.getString(R.string.specify_player_number))
            intent.putExtra("icon", R.drawable.ic_baseline_group)
            intent.putExtra("keyValue", Constants.PLAYERS_NUM_VALUE)
            it.context.startActivity(intent)
        }

        binding.cardSpiesNum.setOnClickListener {
            var intent = Intent(this, ConfigActivity::class.java)
            intent.putExtra("title", resources.getString(R.string.specify_spies_number))
            intent.putExtra("icon", R.drawable.ic_baseline_remove_red_eye)
            intent.putExtra("keyValue", Constants.SPIES_NUM_VALUE)
            it.context.startActivity(intent)
        }

        binding.cardTimer.setOnClickListener {
            var intent = Intent(this, ConfigActivity::class.java)
            intent.putExtra("title", resources.getString(R.string.specify_game_time))
            intent.putExtra("icon", R.drawable.ic_baseline_timer)
            intent.putExtra("keyValue", Constants.TIMERS_NUM_VALUE)
            it.context.startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateValues()
    }

}