package edu.uw.wensix.leetcodelegend.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import edu.uw.wensix.leetcodelegend.R
import edu.uw.wensix.leetcodelegend.databinding.ActivityTimerBinding
import java.util.*

fun navigateToTimerActivity(context: Context) = with(context) {

    val intent = Intent(context, TimerActivity::class.java)

    startActivity(intent)
}

class TimerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimerBinding
    private var pauseOffset: Long = 0

    private lateinit var chronometer: Chronometer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title =  "Timer"
        binding = ActivityTimerBinding.inflate(layoutInflater).apply { setContentView(root) }
        with(binding) {
            val meter: Chronometer = findViewById<Chronometer>(R.id.c_meter)
            playBtn.setOnClickListener(object : View.OnClickListener {
                var isWorking = false

                override fun onClick(v: View) {
                    if (!isWorking) {
                        meter.setBase(SystemClock.elapsedRealtime() - pauseOffset)
                        meter.start()
                        isWorking = true
                    } else {
                        meter.stop()
                        pauseOffset = SystemClock.elapsedRealtime() - meter.base
                        isWorking = false
                    }


                    playBtn.setText(if (isWorking) "PAUSE" else "START")
                    Toast.makeText(
                        this@TimerActivity, getString(
                            if (isWorking)
                                R.string.working
                            else
                                R.string.stopped
                        ),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            })

            resetBtn.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    meter.setBase(SystemClock.elapsedRealtime())
                    pauseOffset = 0
                }
            })



            submitBtn.setOnClickListener {
                val time = SystemClock.elapsedRealtime() - meter.getBase();
                Log.i("time", time.toString())
//                Toast.makeText(this@TimerActivity, "elasp $time", Toast.LENGTH_SHORT).show()
                navigateToEditProblemActivity(this@TimerActivity, time)
            }

            btnPastProblem.setOnClickListener { navigateToPastProblemActivity(this@TimerActivity) }
            problemText.setOnClickListener { navigateToPastProblemActivity(this@TimerActivity) }

        }
    }
}


