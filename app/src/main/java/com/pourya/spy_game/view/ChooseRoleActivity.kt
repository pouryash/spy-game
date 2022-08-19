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
import com.pourya.spy_game.util.SharedPreferenceManager
import com.pourya.spy_game.util.extentions.toast
import com.pourya.spy_game.viewmodel.WordViewModel
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import kotlin.collections.ArrayList

class ChooseRoleActivity : AppCompatActivity() {

    private var playersWord: ArrayList<Word> = ArrayList()
    private lateinit var binding: ActivityChooseRoleBinding
    private var isCardAnimationEnd = true
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
        setupData()
        setupViews()
        changeCameraDistance()
    }

    /**
     * this method read random word and merge spies and players word and shuffle it
     */
    private fun setupData() {
        wordViewModel.getRandomWordWithCat(sharedPreferenceManager.getCategory().id)
        wordViewModel.wordResponseLiveData.observe(this){
            when(it.status){
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    if (it.data != null){
                        for (i in 1..sharedPreferenceManager.getInteger(Constants.SPIES_NUM_VALUE)) {
                            playersWord.add(Word(-1, "you are spy", ""))
                        }
                        while (playersWord.size < sharedPreferenceManager.getInteger(Constants.PLAYERS_NUM_VALUE)) {
                            playersWord.add(Word(it.data!!.data.id, it.data!!.data.name, ""))
                        }
                        playersWord.shuffle()
                    }
                }
                Status.ERROR -> {}
            }
        }
    }

    private fun setupViews() {
        binding.chooseRoleDefaultRoot.root.setOnClickListener {
            if (playersWord.size <= 0 ){
                toast("finish")
                return@setOnClickListener
            }
            if (binding.chooseRoleDefaultRoot.root.isVisible && isCardAnimationEnd) {
                word = playersWord.first()
                binding.chooseRoleShowValueRoot.txtChooseRoleValue.text = word.name
                if (word.id == -1){
                    binding.chooseRoleShowValueRoot.imgCategoryIcon.setImageDrawable(
                        AppCompatResources.getDrawable(this, R.drawable.ic_baseline_eye))
                }else{
                    binding.chooseRoleShowValueRoot.imgCategoryIcon.setImageDrawable(
                        AppCompatResources.getDrawable(this, R.drawable.ic_baseline_group))
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

}