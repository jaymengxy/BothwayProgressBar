package xyz.mengxy.bpb

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.LinearInterpolator
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_set_progress.setOnClickListener {
            val percent = et_positive.text.toString().trim()
            if (percent.isEmpty()) {
                return@setOnClickListener
            }
            progress.setPositivePercent(percent.toFloat())
            val positive: Int = min((percent.toFloat() * 100).toInt(), 100)
            tv_positive.text = positive.toString()
            tv_negative.text = (100 - positive).toString()
        }

        btn_animation_progress.setOnClickListener {
            val percent = et_positive.text.toString().trim()
            if (percent.isEmpty()) {
                return@setOnClickListener
            }
            progress.setPositivePercentWithAnim(percent.toFloat())
            val positive: Int = min((percent.toFloat() * 100).toInt(), 100)
            val positiveAnimator = ValueAnimator.ofInt(0, positive).apply {
                duration = 1000
                interpolator = LinearInterpolator()
                addUpdateListener { animation -> tv_positive.text = (animation?.animatedValue ?: "").toString() }
            }
            val negativeAnimator = ValueAnimator.ofInt(0, 100 - positive).apply {
                duration = 1000
                interpolator = LinearInterpolator()
                addUpdateListener { animation -> tv_negative.text = (animation?.animatedValue ?: "").toString() }
            }
            val set = AnimatorSet()
            set.playTogether(positiveAnimator, negativeAnimator)
            set.start()
        }
    }
}
