package com.example.pomodori

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.pomodori.databinding.ActivityTimeBinding

class TimeActivity : AppCompatActivity() {

    private var session = makeNewSession()
    private lateinit var binding: ActivityTimeBinding

    private fun switchToFragment(type: PhaseType) {
        val fragment =
            if (type == PhaseType.FocusTime) FocusTimeFragment(session)
            else PauseTimeFragment(session)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.aboutButton.setOnClickListener{
            showAboutPopup()
        }

        switchToFragment(PhaseType.FocusTime)
        binding.navView.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.navigation_stats -> {
                    startActivity(Intent(this, StatsActivity::class.java))
                    true
                }
                R.id.navigation_main -> {
                    val currentFragment = binding.fragmentContainer.getFragment<SessionFragment>()
                    session = currentFragment.session
                    val currentFragmentName = currentFragment.javaClass.simpleName


                    if (currentFragmentName != "FocusTimeFragment") {
                        switchToFragment(PhaseType.FocusTime)
                    } else {
                        switchToFragment(PhaseType.PauseTime)
                    }
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    fun showAboutPopup() {
        AboutFragment().show(supportFragmentManager, "ABOUT")
    }
}

