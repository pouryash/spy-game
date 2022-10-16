package com.pourya.spy_game.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.pourya.spy_game.R
import com.pourya.spy_game.databinding.ActivityMainBinding
import com.pourya.spy_game.util.Constants
import com.pourya.spy_game.util.Constants.ZONE_ID_STANDARD_BANNER_MAIN_BANNER
import com.pourya.spy_game.util.SharedPreferenceManager
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusBannerType
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel


import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val sharedPreferenceManager: SharedPreferenceManager by inject()
    private var standardBannerResponseId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        requestTapsellBanner()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setupViews()
        updateValues()
    }

    /**
     * this method request to display ad
     */
    private fun requestTapsellBanner() {
        TapsellPlus.requestStandardBannerAd(
            this, ZONE_ID_STANDARD_BANNER_MAIN_BANNER,
            TapsellPlusBannerType.BANNER_320x50, object : ir.tapsell.plus.AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)
                    standardBannerResponseId = tapsellPlusAdModel.responseId.toString()
                    //Ad is ready to show
                    setupTapsellBanner(tapsellPlusAdModel)
                }

                override fun error(error: String) {
                    super.error(error)
                    Handler(Looper.getMainLooper()).postDelayed({
                        requestTapsellBanner()
                    }, 10000)
                }
            })
    }

    /**
     * this method display add in activity
     */
    private fun setupTapsellBanner(tapsellPlusAdModel: TapsellPlusAdModel) {
        TapsellPlus.showStandardBannerAd(this, tapsellPlusAdModel.responseId,
            findViewById(R.id.standardBannerMain),
            object : AdShowListener() {
                override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onOpened(tapsellPlusAdModel)
                }

                override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                    super.onError(tapsellPlusErrorModel)
                }
            })
    }

    /**
     * this method destroy ad
     */
    private fun destroyAd() {
        standardBannerResponseId?.let {
            TapsellPlus.destroyStandardBanner(
                this,
                standardBannerResponseId,
                findViewById(R.id.standardBannerMain)
            )
        }
    }

    private fun init() {
        if (sharedPreferenceManager.getInteger(Constants.PLAYERS_NUM_VALUE) != 0) {
            return
        }
        sharedPreferenceManager.saveInteger(Constants.PLAYERS_NUM_VALUE, Constants.PLAYER_MIN_VALUE)
        sharedPreferenceManager.saveInteger(Constants.SPIES_NUM_VALUE, Constants.SPIES_MIN_VALUE)
        sharedPreferenceManager.saveInteger(Constants.TIMERS_NUM_VALUE, Constants.TIMER_MIN_VALUE)
    }

    private fun updateValues() {
        binding.txtCategoryValue.text =
            sharedPreferenceManager.getCategory().name.ifEmpty { "All" }
        binding.txtPlayerNumValue.text =
            sharedPreferenceManager.getInteger(Constants.PLAYERS_NUM_VALUE).toString()
        binding.txtSpiesNumValue.text =
            sharedPreferenceManager.getInteger(Constants.SPIES_NUM_VALUE).toString()
        binding.txtTimerValue.text =
            sharedPreferenceManager.getInteger(Constants.TIMERS_NUM_VALUE).toString() + "m"
    }

    private fun setupViews() {
        updateValues()

        binding.btnStartGame.setOnClickListener {
            startActivity(Intent(this, ChooseRoleActivity::class.java))
        }

        binding.cardPlayerNum.setOnClickListener {
            val intent = Intent(this, ConfigActivity::class.java)
            intent.putExtra("title", resources.getString(R.string.specify_player_number))
            intent.putExtra("icon", R.drawable.ic_baseline_group)
            intent.putExtra("keyValue", Constants.PLAYERS_NUM_VALUE)
            it.context.startActivity(intent)
        }

        binding.cardSpiesNum.setOnClickListener {
            val intent = Intent(this, ConfigActivity::class.java)
            intent.putExtra("title", resources.getString(R.string.specify_spies_number))
            intent.putExtra("icon", R.drawable.ic_baseline_eye)
            intent.putExtra("keyValue", Constants.SPIES_NUM_VALUE)
            it.context.startActivity(intent)
        }

        binding.cardTimer.setOnClickListener {
            val intent = Intent(this, ConfigActivity::class.java)
            intent.putExtra("title", resources.getString(R.string.specify_game_time))
            intent.putExtra("icon", R.drawable.ic_baseline_timer)
            intent.putExtra("keyValue", Constants.TIMERS_NUM_VALUE)
            it.context.startActivity(intent)
        }

        binding.cardCategory.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            it.context.startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateValues()
    }

    override fun onDestroy() {
        destroyAd()
        super.onDestroy()
    }

}