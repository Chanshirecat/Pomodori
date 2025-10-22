package com.example.pomodori

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pomodori.databinding.ActivityEditStatsBinding
import com.example.pomodori.databinding.ActivityTimeBinding


private lateinit var binding: ActivityEditStatsBinding


class SingleStatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityEditStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
       .replace(com.example.pomodori.R.id.fragmentContainerStats, StatViewFragment())
       .commit()

    }
}
