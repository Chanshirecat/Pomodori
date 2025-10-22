package com.example.pomodori

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.example.pomodori.databinding.FragmentFocusTimeBinding
import org.w3c.dom.Text

class FocusTimeFragment(session: Session) : SessionFragment(session) {

    private lateinit var timer: CountDownTimer
    private lateinit var binding: FragmentFocusTimeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFocusTimeBinding.inflate(inflater, container, false)

        val durationInMillis = 1500000 // 25min = 25 * 60 * 1000
        timer = object : CountDownTimer(durationInMillis.toLong(), 1000) {
            override fun onTick(remaining: Long) {
                binding.timer.text = formatMillis(remaining)
            }

            override fun onFinish() {
                binding.timer.text = "Done!"
                session = addFocusPhase(session, durationInMillis / 1000)
                writeSessionToDb()
            }
        }

        binding.startbtn.setOnClickListener {
            timer.start()
        }

        return binding.root
    }
}




