package com.pourya.spy_game.view

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import com.pourya.spy_game.R
import com.pourya.spy_game.databinding.ActivityChooseRoleBinding
import com.pourya.spy_game.util.SharedPreferenceManager
import com.pourya.spy_game.util.extentions.toast
import org.koin.android.ext.android.inject

class ChooseRoleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseRoleBinding
    private var isCardAnimationEnd = true
    private val sharedPreferenceManager : SharedPreferenceManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
        changeCameraDistance()

        if (sharedPreferenceManager.getCategory() != null){
            toast(sharedPreferenceManager.getCategory().name)
        }

    }

    private fun setupViews() {
        binding.chooseRoleDefaultRoot.root.setOnClickListener {
            if (binding.chooseRoleDefaultRoot.root.isVisible && isCardAnimationEnd) {
                isCardAnimationEnd = false
                flipCard(
                    this,
                    binding.chooseRoleShowValueRoot.root,
                    binding.chooseRoleDefaultRoot.root
                )
            }
        }
        binding.chooseRoleShowValueRoot.root.setOnClickListener {
            if (binding.chooseRoleShowValueRoot.root.isVisible && isCardAnimationEnd){
                isCardAnimationEnd = false
                flipCard(
                    this,
                    binding.chooseRoleDefaultRoot.root,
                    binding.chooseRoleShowValueRoot.root
                )
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