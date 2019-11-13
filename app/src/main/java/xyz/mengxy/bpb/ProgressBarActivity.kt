package xyz.mengxy.bpb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Mengxy on 2019-11-12.
 */

class ProgressBarActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PERCENT = "extra_percent"

        fun showProgressBar(context: Context, percent: Float) {
            context.startActivity(Intent(context, ProgressBarActivity::class.java).apply {
                putExtra(EXTRA_PERCENT, percent)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}