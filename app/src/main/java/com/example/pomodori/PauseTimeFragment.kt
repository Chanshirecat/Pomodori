package com.example.pomodori

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pomodori.databinding.FragmentPauseTimeBinding


class PauseTimeFragment(session: Session) : SessionFragment(session) {

    private lateinit var timer: CountDownTimer
    private lateinit var binding: FragmentPauseTimeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPauseTimeBinding.inflate(inflater, container, false)

        val durationInMillis = 300000 // 5min = 5 * 60 * 1000
        timer = object : CountDownTimer(durationInMillis.toLong(), 1000) {
            override fun onTick(remaining: Long) {
                binding.timer.text = formatMillis(remaining)
            }

            override fun onFinish() {
                binding.timer.text = "Done!"
                session = addPausePhase(session, durationInMillis / 1000)
                writeSessionToDb()
            }
        }

        binding.startbtn.setOnClickListener {
            timer.start()
        }

        return binding.root
    }

/* animationView.addAnimatorUpdateListener { animation ->
}
animationView.addAnimatorListener()
    animationView.addPauseListener{}
 */

}

