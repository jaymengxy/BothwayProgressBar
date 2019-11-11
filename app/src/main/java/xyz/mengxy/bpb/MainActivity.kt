package xyz.mengxy.bpb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pkl_progress.setPositivePercent(0.4f)
//        pkl_progress.setPositivePercentWithAnim(0.5f)
    }
}
