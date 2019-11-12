package xyz.mengxy.bpb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        pkl_progress.setPositivePercentWithAnim(0.5f)
        btn_set_progress.setOnClickListener {
            val percent = et_positive.text.toString().trim()
            if (percent.isEmpty()) {
                return@setOnClickListener
            }
            pkl_progress.setPositivePercent(percent.toFloat())
        }

        btn_animation_progress.setOnClickListener {
            val percent = et_positive.text.toString().trim()
            if (percent.isEmpty()) {
                return@setOnClickListener
            }
            pkl_progress.setPositivePercentWithAnim(percent.toFloat())
        }
    }
}
