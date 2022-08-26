package com.pourya.spy_game.view

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import com.example.psnews.network.Status
import com.pourya.spy_game.R
import com.pourya.spy_game.databinding.ActivityChooseRoleBinding
import com.pourya.spy_game.model.Word
import com.pourya.spy_game.util.Constants
import com.pourya.spy_game.util.Constants.ZONE_ID_REWARDED_VIDEO_CHOOSE_ROLE
import com.pourya.spy_game.util.SharedPreferenceManager
import com.pourya.spy_game.util.extentions.toast
import com.pourya.spy_game.viewmodel.WordViewModel
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


class ChooseRoleActivity : AppCompatActivity() {

    private var playersWord: ArrayList<Word> = ArrayList()
    private lateinit var binding: ActivityChooseRoleBinding
    private var isCardAnimationEnd = true
    private var standardBannerResponseId: String? = null
    private lateinit var playersCount: Number
    private lateinit var playerNumber: Number
    private val sharedPreferenceManager: SharedPreferenceManager by inject()
    private val wordViewModel: WordViewModel by inject(parameters = {
        parametersOf(
            this@ChooseRoleActivity
        )
    })
    private lateinit var word: Word

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestAd()

        setupData()
        setupViews()
        changeCameraDistance()
    }

    /**
     * this method request to display ad
     */
    private fun requestAd() {
        TapsellPlus.requestRewardedVideoAd(
            this,
            ZONE_ID_REWARDED_VIDEO_CHOOSE_ROLE,
            object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)
                    standardBannerResponseId = tapsellPlusAdModel.responseId.toString()
                    showAd()
                }

                override fun error(message: String) {
                    super.error(message)
                }
            })
    }

    /**
     * this method display add in activity
     */
    private fun showAd() {
        TapsellPlus.showRewardedVideoAd(this, standardBannerResponseId,
            object : AdShowListener() {
                override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onOpened(tapsellPlusAdModel)
                }

                override fun onClosed(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onClosed(tapsellPlusAdModel)
                }

                override fun onRewarded(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onRewarded(tapsellPlusAdModel)
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

    /**
     * this method read random word and merge spies and players word and shuffle it
     */
    private fun setupData() {
        playersCount = sharedPreferenceManager.getInteger(Constants.PLAYERS_NUM_VALUE)

        wordViewModel.getRandomWordWithCat(sharedPreferenceManager.getCategory().id)
        wordViewModel.wordResponseLiveData.observe(this) {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    if (it.data != null) {
                        for (i in 1..sharedPreferenceManager.getInteger(Constants.SPIES_NUM_VALUE)) {
                            playersWord.add(Word(-1, "you are spy", ""))
                        }
                        while (playersWord.size < playersCount.toInt()) {
                            playersWord.add(Word(it.data!!.data.id, it.data!!.data.name, ""))
                        }
                        playersWord.shuffle()

                        calculateCurrentPlayerAndPrint()
                    }
                }
                Status.ERROR -> {}
            }
        }
    }

    /**
     * this method calculate current player number
     */
    private fun calculateCurrentPlayerAndPrint() {
        playerNumber = playersCount.toInt() - (playersWord.size - 1)
        if (playerNumber.toInt() <= playersCount.toInt()) {
            binding.txtPlayerNumber.text = "- player $playerNumber -"
        }
    }

    private fun setupViews() {
        binding.chooseRoleDefaultRoot.root.setOnClickListener {
            if (playersWord.size <= 0) {
                toast("finish")
                return@setOnClickListener
            }
            if (binding.chooseRoleDefaultRoot.root.isVisible && isCardAnimationEnd) {
                word = playersWord.first()
                binding.chooseRoleShowValueRoot.txtChooseRoleValue.text = word.name
                if (word.id == -1) {
                    binding.chooseRoleShowValueRoot.imgCategoryIcon.setImageDrawable(
                        AppCompatResources.getDrawable(this, R.drawable.ic_baseline_eye)
                    )
                } else {
                    binding.chooseRoleShowValueRoot.imgCategoryIcon.setImageDrawable(
                        AppCompatResources.getDrawable(this, R.drawable.ic_baseline_group)
                    )
                }
                isCardAnimationEnd = false
                flipCard(
                    this,
                    binding.chooseRoleShowValueRoot.root,
                    binding.chooseRoleDefaultRoot.root
                )
            }
        }
        binding.chooseRoleShowValueRoot.root.setOnClickListener {
            if (binding.chooseRoleShowValueRoot.root.isVisible && isCardAnimationEnd) {
                isCardAnimationEnd = false
                flipCard(
                    this,
                    binding.chooseRoleDefaultRoot.root,
                    binding.chooseRoleShowValueRoot.root
                )
                playersWord.remove(word)

                calculateCurrentPlayerAndPrint()
            }
        }
    }

    private fun changeCameraDistance() {
        val distance = 8000
        val scale = resources.displayMetrics.density * distance
        binding.chooseRoleShowValueRoot.root.cameraDistance = scale
        binding.chooseRoleDefaultRoot.root.cameraDistance = scale
    }

    fun flipCard(context: Context, visibleView: View, inVisibleView: View) {

        visibleView.visibility = View.VISIBLE
        val flipOutAnimatorSet =
            AnimatorInflater.loadAnimator(
                context,
                R.animator.flip_out
            ) as AnimatorSet
        flipOutAnimatorSet.setTarget(inVisibleView)
        val flipInAnimationSet =
            AnimatorInflater.loadAnimator(
                context,
                R.animator.flip_in
            ) as AnimatorSet
        flipInAnimationSet.setTarget(visibleView)
        flipOutAnimatorSet.start()
        flipInAnimationSet.start()
        flipInAnimationSet.doOnEnd {
            inVisibleView.visibility = View.GONE
            isCardAnimationEnd = true
        }
    }

    override fun onDestroy() {
        destroyAd()
        super.onDestroy()
    }

}